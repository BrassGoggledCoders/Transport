package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.manager.IManager;
import xyz.brassgoggledcoders.transport.api.manager.Manager;
import xyz.brassgoggledcoders.transport.api.manager.ManagerType;
import xyz.brassgoggledcoders.transport.block.YardMasterBlock;
import xyz.brassgoggledcoders.transport.container.provider.ManagerContainerProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class YardMasterTileEntity extends TileEntity implements ITickableTileEntity {
    private final Manager manager;
    private final LazyOptional<IManager> managerLazy;

    public YardMasterTileEntity(TileEntityType<? extends YardMasterTileEntity> tileEntityType) {
        super(tileEntityType);
        this.manager = new Manager(this::getPos, this::createBoundaries, ManagerType.RAIL, nothing -> this.markDirty());
        this.managerLazy = LazyOptional.of(this::getManager);
    }

    @Override
    public void tick() {

    }

    @Nonnull
    public AxisAlignedBB createBoundaries() {
        Direction facing = this.getBlockState().get(YardMasterBlock.FACING);
        BlockPos blockPos = this.getPos();
        BlockPos farCorner = blockPos.offset(facing, 8).offset(facing.rotateY(), 4).up(6);
        BlockPos nearCorner = blockPos.offset(facing.rotateYCCW(), 4).down(2);
        return new AxisAlignedBB(farCorner.getX(), farCorner.getY(), farCorner.getZ(),
                nearCorner.getX(), nearCorner.getY(), nearCorner.getZ());
    }

    public ActionResultType onRightClick(PlayerEntity playerEntity) {
        if (!playerEntity.isSecondaryUseActive()) {
            if (playerEntity instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) playerEntity, new ManagerContainerProvider(this,
                        this.getManager()), manager::writeToPacketBuffer);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == TransportAPI.MANAGER) {
            return managerLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.managerLazy.invalidate();
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);
        nbt.put("manager", manager.serializeNBT());
        return nbt;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        manager.deserializeNBT(nbt.getCompound("manager"));
    }

    @Nonnull
    public IManager getManager() {
        return this.manager;
    }
}

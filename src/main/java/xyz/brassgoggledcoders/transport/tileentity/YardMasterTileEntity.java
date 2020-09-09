package xyz.brassgoggledcoders.transport.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.block.YardMasterBlock;
import xyz.brassgoggledcoders.transport.container.provider.YardMasterContainerProvider;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

public class YardMasterTileEntity extends TileEntity implements ITickableTileEntity {
    private UUID uniqueId;
    private AxisAlignedBB yardBoundaries;
    private List<YardMasterObject> connectedObjects;

    public YardMasterTileEntity(TileEntityType<? extends YardMasterTileEntity> tileEntityType) {
        super(tileEntityType);
        this.uniqueId = UUID.randomUUID();
        this.connectedObjects = Lists.newArrayList();
    }

    @Override
    public void tick() {
        if (yardBoundaries == null && this.getWorld() != null) {
            Direction facing = this.getBlockState().get(YardMasterBlock.FACING);
            BlockPos blockPos = this.getPos();
            BlockPos farCorner = blockPos.offset(facing, 8).offset(facing.rotateY(), 4).up(6);
            BlockPos nearCorner = blockPos.offset(facing.rotateYCCW(), 4).down(2);
            this.yardBoundaries = new AxisAlignedBB(farCorner.getX(), farCorner.getY(), farCorner.getZ(),
                    nearCorner.getX(), nearCorner.getY(), nearCorner.getZ());
        }
    }

    public ActionResultType onRightClick(PlayerEntity playerEntity) {
        if (!playerEntity.isSecondaryUseActive()) {
            if (playerEntity instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) playerEntity, new YardMasterContainerProvider(this),
                        packetBuffer -> {
                            packetBuffer.writeInt(this.connectedObjects.size());
                            for (YardMasterObject yardMasterObject : this.connectedObjects) {
                                packetBuffer.writeLong(yardMasterObject.getBlockPos().toLong());
                                packetBuffer.writeItemStack(yardMasterObject.getRepresentative());
                            }
                        });
            }
        }
        return ActionResultType.PASS;
    }

    public boolean addConnectedObject(YardMasterObject yardMasterObject) {
        BlockPos objectPos = yardMasterObject.getBlockPos();
        if (yardBoundaries != null && yardBoundaries.contains(objectPos.getX(), objectPos.getY(), objectPos.getZ()) &&
                !objectPos.equals(this.getPos())) {
            return this.connectedObjects.add(yardMasterObject);
        } else {
            return false;
        }
    }

    @Override
    public void setPos(@Nonnull BlockPos blockPos) {
        super.setPos(blockPos);
        this.handleNewPos();
    }

    @Override
    public void setWorldAndPos(@Nonnull World world, @Nonnull BlockPos pos) {
        super.setWorldAndPos(world, pos);
        this.handleNewPos();
    }

    private void handleNewPos() {
        this.yardBoundaries = null;
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);
        nbt.putUniqueId("uniqueId", this.uniqueId);
        ListNBT connectedObjectNBT = new ListNBT();
        for (YardMasterObject yardMasterObject : this.connectedObjects) {
            connectedObjectNBT.add(yardMasterObject.toCompoundNBT());
        }
        nbt.put("connectedObjects", connectedObjectNBT);
        return nbt;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.uniqueId = nbt.getUniqueId("uniqueId");
        this.connectedObjects.clear();
        ListNBT connectedObjectNBT = nbt.getList("connectedObjects", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < connectedObjectNBT.size(); i++) {
            this.connectedObjects.add(new YardMasterObject(connectedObjectNBT.getCompound(i)));
        }
    }

    public List<YardMasterObject> getConnectedObjects() {
        return connectedObjects;
    }
}

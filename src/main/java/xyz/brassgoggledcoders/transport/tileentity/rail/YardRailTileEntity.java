package xyz.brassgoggledcoders.transport.tileentity.rail;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.master.IManageable;
import xyz.brassgoggledcoders.transport.api.master.Manageable;
import xyz.brassgoggledcoders.transport.api.master.ManagerType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class YardRailTileEntity extends TileEntity {
    private final IManageable manageable;
    private final LazyOptional<IManageable> manageableLazy;

    public YardRailTileEntity(TileEntityType<? extends YardRailTileEntity> tileEntityType) {
        super(tileEntityType);
        this.manageable = new Manageable(this::getPos, this::getRepresentation, ManagerType.RAIL);
        this.manageableLazy = LazyOptional.of(() -> manageable);
    }

    @Nonnull
    public ItemStack getRepresentation() {
        return new ItemStack(this.getBlockState().getBlock());
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == TransportAPI.MANAGEABLE) {
            return manageableLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt = super.write(nbt);
        nbt.put("manageable", this.manageable.serializeNBT());
        return nbt;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.manageable.deserializeNBT(nbt.getCompound("manageable"));
    }
}

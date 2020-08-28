package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class PodiumTileEntity extends TileEntity {
    private ItemStack displayItemStack = ItemStack.EMPTY;

    public PodiumTileEntity(TileEntityType<? extends PodiumTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    public ItemStack getDisplayItemStack() {
        return this.displayItemStack;
    }

    public void setDisplayItemStack(ItemStack itemStack) {
        this.displayItemStack = itemStack;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState blockState, CompoundNBT nbt) {
        super.read(blockState, nbt);
        this.setDisplayItemStack(ItemStack.read(nbt.getCompound("displayItemStack")));
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("displayItemStack", this.getDisplayItemStack().write(new CompoundNBT()));
        return nbt;
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt =  super.getUpdateTag();
        nbt.put("displayItemStack", this.getDisplayItemStack().write(new CompoundNBT()));
        return nbt;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {

    }
}

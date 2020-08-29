package xyz.brassgoggledcoders.transport.api.podium;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IPodium {
    @Nonnull
    BlockPos getPodiumPos();

    @Nonnull
    BlockState getPodiumBlockState();

    @Nonnull
    ItemStack getDisplayItemStack();

    void setDisplayItemStack(@Nonnull ItemStack itemStack);

    @Nonnull
    IWorld getPodiumWorld();

    void pulseRedstone(int power);

    @Nonnull
    PodiumInventory getPodiumInventory();
}

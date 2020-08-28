package xyz.brassgoggledcoders.transport.api.podium;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nonnull;

public interface IPodium {
    @Nonnull
    BlockPos getPodiumPos();

    @Nonnull
    BlockState getPodiumBlockState();

    @Nonnull
    ItemStack getDisplayItemStack();

    @Nonnull
    IWorld getPodiumWorld();

    void pulseRedstone(int power);
}

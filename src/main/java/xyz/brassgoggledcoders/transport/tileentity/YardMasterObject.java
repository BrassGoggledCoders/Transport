package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class YardMasterObject {
    private final BlockPos blockPos;
    private final ItemStack representative;

    public YardMasterObject(BlockPos blockPos, ItemStack representative) {
        this.blockPos = blockPos;
        this.representative = representative;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public ItemStack getRepresentative() {
        return representative;
    }
}

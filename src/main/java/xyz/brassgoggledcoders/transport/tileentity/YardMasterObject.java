package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class YardMasterObject {
    private final BlockPos blockPos;
    private final ItemStack representative;

    public YardMasterObject(BlockPos blockPos, ItemStack representative) {
        this.blockPos = blockPos;
        this.representative = representative;
    }

    public YardMasterObject(CompoundNBT compound) {
        this(BlockPos.fromLong(compound.getLong("blockPos")), ItemStack.read(compound.getCompound("representative")));
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public ItemStack getRepresentative() {
        return representative;
    }

    public CompoundNBT toCompoundNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("blockPos", blockPos.toLong());
        nbt.put("representative", this.representative.write(new CompoundNBT()));
        return nbt;
    }
}

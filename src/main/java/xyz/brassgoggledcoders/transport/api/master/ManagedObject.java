package xyz.brassgoggledcoders.transport.api.master;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class ManagedObject {
    private final BlockPos blockPos;
    private final ItemStack representative;

    public ManagedObject(BlockPos blockPos, ItemStack representative) {
        this.blockPos = blockPos;
        this.representative = representative;
    }

    public ManagedObject(CompoundNBT compound) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManagedObject)) {
            return false;
        }
        ManagedObject that = (ManagedObject) o;
        return Objects.equals(this.getBlockPos(), that.getBlockPos()) &&
                this.getRepresentative().equals(that.getRepresentative(), true);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getBlockPos(), this.getRepresentative());
    }
}

package xyz.brassgoggledcoders.transport.capability.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class BasicEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {
    public BasicEnergyStorage(int capacity) {
        super(capacity);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("energy", this.energy);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
    }

    public int getComparatorLevel() {
        return (int) (15F * ((float) this.energy / (float) this.getMaxEnergyStored()));
    }
}

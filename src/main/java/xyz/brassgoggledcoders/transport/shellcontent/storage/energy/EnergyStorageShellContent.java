package xyz.brassgoggledcoders.transport.shellcontent.storage.energy;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public class EnergyStorageShellContent extends ShellContent {
    private final EnergyStorage energyStorage;

    public EnergyStorageShellContent(int capacity, int maxReceive, int maxExtract, boolean creative) {
        this.energyStorage = new EnergyStorage(capacity, maxReceive, maxExtract);
    }

    @NotNull
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @NotNull
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        compoundTag.put("energy", this.getEnergyStorage().serializeNBT());
        return compoundTag;
    }

    @Override
    public void deserializeNBT(@NotNull CompoundTag compoundTag) {
        super.deserializeNBT(compoundTag);
        this.getEnergyStorage()
                .deserializeNBT(compoundTag.getCompound("energy"));
    }
}

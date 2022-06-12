package xyz.brassgoggledcoders.transport.shellcontent.storage.energy;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public class EnergyStorageShellContent extends ShellContent {
    private final EnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> lazyOptional;

    public EnergyStorageShellContent(int capacity, int maxReceive, int maxExtract) {
        this.energyStorage = new EnergyStorage(capacity, maxReceive, maxExtract);
        this.lazyOptional = LazyOptional.of(this::getEnergyStorage);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return this.lazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyOptional.invalidate();
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
        this.getEnergyStorage().deserializeNBT(compoundTag.get("energy"));
    }
}

package xyz.brassgoggledcoders.transport.api.manager;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SingleCapabilityProvider implements ICapabilityProvider {
    private LazyOptional<?> currentCap;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return currentCap.cast();
    }

    public SingleCapabilityProvider withCurrentCap(LazyOptional<?> currentCap) {
        this.currentCap = currentCap;
        return this;
    }
}

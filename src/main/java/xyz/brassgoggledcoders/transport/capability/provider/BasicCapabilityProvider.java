package xyz.brassgoggledcoders.transport.capability.provider;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicCapabilityProvider<T> implements ICapabilityProvider {
    private final LazyOptional<T> instanceLazy;
    private final Capability<T> capability;

    public BasicCapabilityProvider(Capability<T> capability, T instance) {
        this.instanceLazy = LazyOptional.of(() -> instance);
        this.capability = capability;
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        return cap == this.capability ? instanceLazy.cast() : LazyOptional.empty();
    }

    public void invalidate() {
        instanceLazy.invalidate();
    }
}

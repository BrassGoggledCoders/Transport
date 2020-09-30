package xyz.brassgoggledcoders.transport.capability.provider;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NBTCapabilityProvider<T extends INBTSerializable<CompoundNBT>> implements ICapabilitySerializable<CompoundNBT> {
    private final T instance;
    private final LazyOptional<T> instanceLazy;
    private final Capability<T> capability;

    public NBTCapabilityProvider(Capability<T> capability, T instance) {
        this.instance = instance;
        this.instanceLazy = LazyOptional.of(() -> instance);
        this.capability = capability;
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        return cap == this.capability ? instanceLazy.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        instance.deserializeNBT(nbt);
    }

    public void invalidate() {
        instanceLazy.invalidate();
    }
}

package xyz.brassgoggledcoders.transport.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CapabilityProvider<CAP, VALUE extends CAP> implements ICapabilitySerializable<CompoundTag> {
    private final LazyOptional<CAP> lazyOptional;
    private final Capability<CAP> type;
    private final NonNullSupplier<VALUE> constructor;
    private final Function<CompoundTag, VALUE> reader;
    private final Function<VALUE, CompoundTag> writer;

    private VALUE value;

    public CapabilityProvider(Capability<CAP> type, NonNullSupplier<VALUE> constructor, Function<CompoundTag, VALUE> reader,
                              Function<VALUE, CompoundTag> writer) {
        this.type = type;
        this.constructor = constructor;
        this.reader = reader;
        this.writer = writer;
        this.lazyOptional = LazyOptional.of(this::getValue);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.type.orEmpty(cap, this.lazyOptional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.writer.apply(this.getValue());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.value = this.reader.apply(nbt);
    }

    @NotNull
    public VALUE getValue() {
        if (this.value == null) {
            this.value = constructor.get();
        }
        return this.value;
    }
}

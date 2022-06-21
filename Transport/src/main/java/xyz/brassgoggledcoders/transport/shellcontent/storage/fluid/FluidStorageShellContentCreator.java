package xyz.brassgoggledcoders.transport.shellcontent.storage.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IFluidStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.content.TransportShellContentTypes;

public record FluidStorageShellContentCreator(
        int capacity,
        boolean allowItemInteraction
) implements IFluidStorageShellContentCreator<FluidStorageShellContent> {
    public static Codec<IFluidStorageShellContentCreator<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("capacity")
                    .forGetter(IFluidStorageShellContentCreator::getCapacity),
            Codec.BOOL.optionalFieldOf("allowItemInteraction", true)
                    .forGetter(IFluidStorageShellContentCreator::isAllowItemInteraction)
    ).apply(instance, FluidStorageShellContentCreator::new));

    @Override
    public boolean isAllowItemInteraction() {
        return this.allowItemInteraction();
    }

    @Override
    public int getCapacity() {
        return this.capacity();
    }

    @NotNull
    @Override
    public FluidStorageShellContent get() {
        return new FluidStorageShellContent(capacity, allowItemInteraction);
    }

    @Override
    public ShellContentType<?> getType() {
        return TransportShellContentTypes.FLUID_STORAGE.get();
    }
}

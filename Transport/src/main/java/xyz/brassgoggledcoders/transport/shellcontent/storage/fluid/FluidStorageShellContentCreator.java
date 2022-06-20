package xyz.brassgoggledcoders.transport.shellcontent.storage.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.content.TransportShellContentTypes;

public record FluidStorageShellContentCreator(
        int capacity,
        boolean allowItemInteraction
) implements IShellContentCreator<FluidStorageShellContent> {
    public static Codec<FluidStorageShellContentCreator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("capacity")
                    .forGetter(FluidStorageShellContentCreator::capacity),
            Codec.BOOL.optionalFieldOf("allowItemInteraction", true)
                    .forGetter(FluidStorageShellContentCreator::allowItemInteraction)
    ).apply(instance, FluidStorageShellContentCreator::new));

    @NotNull
    @Override
    public FluidStorageShellContent get() {
        return new FluidStorageShellContent(capacity, allowItemInteraction);
    }

    @Override
    public ShellContentType<?, ?> getType() {
        return TransportShellContentTypes.FLUID_STORAGE.get();
    }
}

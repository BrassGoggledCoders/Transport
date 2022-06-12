package xyz.brassgoggledcoders.transport.shellcontent.storage.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.content.TransportShellContentTypes;

public record EnergyStorageShellContentCreator(
        int capacity,
        int maxReceive,
        int maxExtract
) implements IShellContentCreator<EnergyStorageShellContent> {
    public static final Codec<EnergyStorageShellContentCreator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("capacity").forGetter(EnergyStorageShellContentCreator::capacity),
            Codec.INT.optionalFieldOf("maxReceive", -1).forGetter(EnergyStorageShellContentCreator::maxReceive),
            Codec.INT.optionalFieldOf("maxExtract", -1).forGetter(EnergyStorageShellContentCreator::maxExtract)
    ).apply(instance, EnergyStorageShellContentCreator::new));


    @Override
    public ShellContentType<?, ?> getType() {
        return TransportShellContentTypes.ENERGY_STORAGE.get();
    }

    @NotNull
    @Override
    public EnergyStorageShellContent get() {
        return new EnergyStorageShellContent(
                this.capacity(),
                this.maxReceive() >= 0 ? this.maxReceive() : this.capacity(),
                this.maxExtract() >= 0 ? this.maxExtract() : this.capacity()
        );
    }
}

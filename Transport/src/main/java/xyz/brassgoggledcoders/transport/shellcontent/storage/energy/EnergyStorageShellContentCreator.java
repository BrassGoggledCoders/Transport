package xyz.brassgoggledcoders.transport.shellcontent.storage.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IEnergyStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.content.TransportShellContentTypes;

public record EnergyStorageShellContentCreator(
        int capacity,
        int maxReceive,
        int maxExtract,
        boolean creative
) implements IEnergyStorageShellContentCreator<EnergyStorageShellContent> {
    public static final Codec<IEnergyStorageShellContentCreator<? extends ShellContent>> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("capacity").forGetter(IEnergyStorageShellContentCreator::getCapacity),
                    Codec.INT.optionalFieldOf("maxReceive", -1).forGetter(IEnergyStorageShellContentCreator::getMaxReceive),
                    Codec.INT.optionalFieldOf("maxExtract", -1).forGetter(IEnergyStorageShellContentCreator::getMaxExtract),
                    Codec.BOOL.optionalFieldOf("creative", false).forGetter(IEnergyStorageShellContentCreator::isCreative)
            ).apply(instance, EnergyStorageShellContentCreator::new));


    @Override
    public ShellContentType<?> getType() {
        return TransportShellContentTypes.ENERGY_STORAGE.get();
    }

    @NotNull
    @Override
    public EnergyStorageShellContent get() {
        return new EnergyStorageShellContent(
                this.capacity(),
                this.maxReceive() >= 0 ? this.maxReceive() : this.capacity(),
                this.maxExtract() >= 0 ? this.maxExtract() : this.capacity(),
                this.creative()
        );
    }

    @Override
    public int getCapacity() {
        return this.capacity();
    }

    @Override
    public int getMaxReceive() {
        return this.maxReceive();
    }

    @Override
    public int getMaxExtract() {
        return this.maxExtract();
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}

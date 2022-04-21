package xyz.brassgoggledcoders.transport.data.shellcontent.builders;

import net.minecraftforge.fluids.FluidAttributes;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.data.shellcontent.IShellContentCreatorBuilder;
import xyz.brassgoggledcoders.transport.shellcontent.storage.energy.EnergyStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.fluid.FluidStorageShellContentCreator;

public class EnergyStorageShellContentBuilder implements IShellContentCreatorBuilder {
    private final int capacity;

    private int maxReceive;
    private int maxExtract;

    public EnergyStorageShellContentBuilder(int capacity) {
        this.capacity = capacity;
        this.maxReceive = capacity;
        this.maxExtract = capacity;
    }

    public EnergyStorageShellContentBuilder withMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
        return this;
    }

    public EnergyStorageShellContentBuilder withMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
        return this;
    }

    @Override
    public IShellContentCreator<?> build() {
        return new EnergyStorageShellContentCreator(
                capacity,
                maxReceive,
                maxExtract
        );
    }

    public static EnergyStorageShellContentBuilder of(int capacity) {
        return new EnergyStorageShellContentBuilder(capacity);
    }
}

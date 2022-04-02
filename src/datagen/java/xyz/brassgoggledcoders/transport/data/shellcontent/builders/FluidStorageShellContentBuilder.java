package xyz.brassgoggledcoders.transport.data.shellcontent.builders;

import net.minecraftforge.fluids.FluidAttributes;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.data.shellcontent.IShellContentCreatorBuilder;
import xyz.brassgoggledcoders.transport.shellcontent.storage.fluid.FluidStorageShellContentCreator;

public class FluidStorageShellContentBuilder implements IShellContentCreatorBuilder {
    private final int capacity;
    private boolean allowItemInteraction;

    public FluidStorageShellContentBuilder(int capacity) {
        this.capacity = capacity;
        this.allowItemInteraction = true;
    }

    public FluidStorageShellContentBuilder withAllowItemInteraction(boolean allowItemInteraction) {
        this.allowItemInteraction = allowItemInteraction;
        return this;
    }

    @Override
    public IShellContentCreator<?> build() {
        return new FluidStorageShellContentCreator(
                capacity,
                allowItemInteraction
        );
    }

    public static FluidStorageShellContentBuilder of(int capacity) {
        return new FluidStorageShellContentBuilder(capacity);
    }

    public static FluidStorageShellContentBuilder ofBuckets(int buckets) {
        return of(FluidAttributes.BUCKET_VOLUME * buckets);
    }
}

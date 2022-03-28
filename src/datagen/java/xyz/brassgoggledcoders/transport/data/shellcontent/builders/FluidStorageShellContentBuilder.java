package xyz.brassgoggledcoders.transport.data.shellcontent.builders;

import net.minecraftforge.fluids.FluidAttributes;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.data.shellcontent.IShellContentCreatorBuilder;
import xyz.brassgoggledcoders.transport.shellcontent.storage.fluid.FluidStorageShellContentCreator;

public record FluidStorageShellContentBuilder(int capacity) implements IShellContentCreatorBuilder {
    @Override
    public IShellContentCreator<?> build() {
        return new FluidStorageShellContentCreator(
                capacity
        );
    }

    public static FluidStorageShellContentBuilder of(int capacity) {
        return new FluidStorageShellContentBuilder(capacity);
    }

    public static FluidStorageShellContentBuilder ofBuckets(int buckets) {
        return of(FluidAttributes.BUCKET_VOLUME * buckets);
    }
}

package xyz.brassgoggledcoders.transport.data.shellcontent.builder;

import com.mojang.serialization.Codec;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IFluidStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.data.shellcontent.IShellContentCreatorBuilder;
import xyz.brassgoggledcoders.transport.shellcontent.storage.fluid.FluidStorageShellContentCreator;

public class FluidStorageShellContentBuilder implements IShellContentCreatorBuilder, IFluidStorageShellContentCreator<ShellContent> {
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
        return this;
    }

    @Override
    public Codec<? extends IShellContentCreator<?>> getCodec() {
        return FluidStorageShellContentCreator.CODEC;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public boolean isAllowItemInteraction() {
        return this.allowItemInteraction;
    }

    @NotNull
    @Override
    public ShellContent get() {
        throw new IllegalStateException("Should only be used in Data");
    }

    public static FluidStorageShellContentBuilder of(int capacity) {
        return new FluidStorageShellContentBuilder(capacity);
    }

    public static FluidStorageShellContentBuilder ofBuckets(int buckets) {
        return of(FluidType.BUCKET_VOLUME * buckets);
    }
}

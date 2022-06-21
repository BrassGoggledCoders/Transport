package xyz.brassgoggledcoders.transport.data.shellcontent.builder;

import com.google.common.base.Suppliers;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IFluidStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.data.shellcontent.IShellContentCreatorBuilder;

import java.util.function.Supplier;

public class FluidStorageShellContentBuilder implements IShellContentCreatorBuilder, IFluidStorageShellContentCreator<ShellContent> {

    private static final Supplier<ShellContentType<?>> SHELL_CONTENT_TYPE = Suppliers.memoize(
            () -> RegistryManager.ACTIVE.getRegistry(TransportAPI.SHELL_CONTENT_TYPE_KEY)
                    .getValue(TransportAPI.rl("fluid_storage"))
    );

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
    public ShellContentType<?> getType() {
        return SHELL_CONTENT_TYPE.get();
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
        return of(FluidAttributes.BUCKET_VOLUME * buckets);
    }
}

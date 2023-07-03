package xyz.brassgoggledcoders.transport.data.shellcontent.builder;

import com.google.common.base.Suppliers;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IEnergyStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.data.shellcontent.IShellContentCreatorBuilder;

import java.util.function.Supplier;

public class EnergyStorageShellContentBuilder implements IShellContentCreatorBuilder,
        IEnergyStorageShellContentCreator<ShellContent> {

    private static final Supplier<ShellContentType<?>> SHELL_CONTENT_TYPE = Suppliers.memoize(
            () -> RegistryManager.ACTIVE.getRegistry(TransportAPI.SHELL_CONTENT_TYPE_KEY)
                    .getValue(TransportAPI.rl("energy_storage"))
    );

    private final int capacity;

    private int maxReceive;
    private int maxExtract;

    private boolean creative;

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

    public EnergyStorageShellContentBuilder withCreative(boolean creative) {
        this.creative = creative;
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
        return capacity;
    }

    @Override
    public int getMaxReceive() {
        return maxReceive;
    }

    @Override
    public int getMaxExtract() {
        return maxExtract;
    }

    @Override
    public boolean isCreative() {
        return creative;
    }

    @NotNull
    @Override
    public ShellContent get() {
        throw new IllegalStateException("This Class only exists for Data");
    }

    public static EnergyStorageShellContentBuilder of(int capacity) {
        return new EnergyStorageShellContentBuilder(capacity);
    }
}

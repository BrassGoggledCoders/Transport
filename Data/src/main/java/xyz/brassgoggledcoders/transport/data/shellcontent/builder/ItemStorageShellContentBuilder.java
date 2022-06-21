package xyz.brassgoggledcoders.transport.data.shellcontent.builder;

import com.google.common.base.Suppliers;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IItemStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.StorageSize;
import xyz.brassgoggledcoders.transport.data.shellcontent.IShellContentCreatorBuilder;

import java.util.function.Supplier;

public class ItemStorageShellContentBuilder implements IShellContentCreatorBuilder, IItemStorageShellContentCreator<ShellContent> {
    private static final Supplier<ShellContentType<?>> SHELL_CONTENT_TYPE = Suppliers.memoize(
            () -> RegistryManager.ACTIVE.getRegistry(TransportAPI.SHELL_CONTENT_TYPE_KEY)
                    .getValue(TransportAPI.rl("item_storage"))
    );

    private final StorageSize size;
    private boolean showScreen = true;

    public ItemStorageShellContentBuilder(StorageSize size) {
        this.size = size;
    }

    public ItemStorageShellContentBuilder withShowScreen(boolean showScreen) {
        this.showScreen = showScreen;
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
    public StorageSize getSize() {
        return this.size;
    }

    @Override
    public boolean isShowScreen() {
        return this.showScreen;
    }

    @NotNull
    @Override
    public ShellContent get() {
        throw new IllegalStateException("Should only be used in Data");
    }

    public static ItemStorageShellContentBuilder of(StorageSize size) {
        return new ItemStorageShellContentBuilder(size);
    }
}

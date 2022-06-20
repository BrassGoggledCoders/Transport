package xyz.brassgoggledcoders.transport.data.shellcontent.builders;

import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.data.shellcontent.IShellContentCreatorBuilder;
import xyz.brassgoggledcoders.transport.shellcontent.storage.item.ItemStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.item.StorageSize;

public class ItemStorageShellContentBuilder implements IShellContentCreatorBuilder {
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
        return new ItemStorageShellContentCreator(
                size,
                showScreen
        );
    }

    public static ItemStorageShellContentBuilder of(StorageSize size) {
        return new ItemStorageShellContentBuilder(size);
    }
}

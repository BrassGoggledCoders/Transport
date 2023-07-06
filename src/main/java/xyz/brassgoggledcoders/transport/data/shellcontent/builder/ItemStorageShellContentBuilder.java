package xyz.brassgoggledcoders.transport.data.shellcontent.builder;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IItemStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.StorageSize;
import xyz.brassgoggledcoders.transport.data.shellcontent.IShellContentCreatorBuilder;
import xyz.brassgoggledcoders.transport.shellcontent.storage.item.ItemStorageShellContentCreator;

public class ItemStorageShellContentBuilder implements IShellContentCreatorBuilder, IItemStorageShellContentCreator<ShellContent> {

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
    public Codec<? extends IShellContentCreator<?>> getCodec() {
        return ItemStorageShellContentCreator.CODEC;
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

package xyz.brassgoggledcoders.transport.shellcontent.storage.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.content.TransportShellContentTypes;

public record ItemStorageShellContentCreator(
        StorageSize size,
        boolean showScreen
) implements IShellContentCreator<ItemStorageShellContent> {
    public static final Codec<ItemStorageShellContentCreator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.flatXmap(
                    value -> StorageSize.getByName(value)
                            .map(DataResult::success)
                            .orElseGet(() -> DataResult.error("No Size with name '" + value + "' exists.")),
                    value -> DataResult.success(value.name())
            ).fieldOf("size").forGetter(ItemStorageShellContentCreator::size),
            Codec.BOOL.optionalFieldOf("showScreen", Boolean.TRUE).forGetter(ItemStorageShellContentCreator::showScreen)
    ).apply(instance, ItemStorageShellContentCreator::new));


    @Override
    public ShellContentType<?> getType() {
        return TransportShellContentTypes.ITEM_STORAGE.get();
    }

    @NotNull
    @Override
    public ItemStorageShellContent get() {
        return new ItemStorageShellContent(
                size,
                showScreen
        );
    }
}

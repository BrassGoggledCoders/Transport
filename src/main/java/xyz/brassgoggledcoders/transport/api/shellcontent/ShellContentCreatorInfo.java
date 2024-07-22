package xyz.brassgoggledcoders.transport.api.shellcontent;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.attachment.ShellContentItemAttachment;
import xyz.brassgoggledcoders.transport.content.TransportAttachments;
import xyz.brassgoggledcoders.transport.content.TransportShellContent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public record ShellContentCreatorInfo(
        BlockState viewState,
        Component name,
        boolean createRecipe,
        IShellContentCreator<?> contentCreator
) {
    @SuppressWarnings("RedundantTypeArguments") //IShellContentCreator<?> is necessary for the dispatch or code fails???
    private static final Codec<ShellContentCreatorInfo> CODEC = ExtraCodecs.lazyInitializedCodec(() ->
            RecordCodecBuilder.create(instance -> instance.group(
                    BlockState.CODEC.fieldOf("view_state")
                            .forGetter(ShellContentCreatorInfo::viewState),
                    ComponentSerialization.CODEC.optionalFieldOf("name")
                            .forGetter(creatorInfo -> Optional.of(creatorInfo.name())),
                    Codec.BOOL.optionalFieldOf("createRecipe", Boolean.TRUE)
                            .forGetter(ShellContentCreatorInfo::createRecipe),
                    TransportShellContent.SHELL_CONTENT_TYPES
                            .byNameCodec()
                            .<IShellContentCreator<?>>dispatch(IShellContentCreator::getCodec, Function.identity())
                            .fieldOf("content")
                            .forGetter(ShellContentCreatorInfo::contentCreator)
            ).apply(instance, (viewState, name, createRecipe, content) -> new ShellContentCreatorInfo(
                    viewState,
                    name.orElseGet(() -> viewState.getBlock().getName()),
                    createRecipe,
                    content)
            ))
    );

    public static final String NBT_TAG_ELEMENT = "ShellContent";
    public static final String NBT_TAG_ID = "Id";
    public static final String NBT_TAG_DATA = "Data";

    public ShellContent create(@Nullable CompoundTag nbt) {
        ShellContent shellContent = this.contentCreator().get();
        shellContent.setCreatorInfo(this);
        if (nbt != null && !nbt.isEmpty()) {
            shellContent.deserializeNBT(nbt);
        }
        return shellContent;
    }

    public Optional<CompoundTag> asTag() {
        return ShellContentCreatorInfo.getCodec()
                .encode(this, NbtOps.INSTANCE, NbtOps.INSTANCE.empty())
                .result()
                .filter(CompoundTag.class::isInstance)
                .map(CompoundTag.class::cast);
    }

    public ItemStack embedNBT(ItemStack itemStack) {
        itemStack.setData(
                TransportAttachments.SHELL_CONTENT_ITEM,
                new ShellContentItemAttachment(
                        TransportAPI.SHELL_CONTENT_CREATOR.get()
                                .getId(this)
                )
        );
        return itemStack;
    }

    public static ShellContentCreatorInfo fromTag(CompoundTag tag) {
        return ShellContentCreatorInfo.getCodec().decode(NbtOps.INSTANCE, tag)
                .resultOrPartial(error -> TransportAPI.LOGGER.warn("Failed to decode Creator info. Error {}, Tag {}", error, tag))
                .map(Pair::getFirst)
                .orElseGet(TransportAPI.SHELL_CONTENT_CREATOR.get()::getEmpty);
    }

    public static Codec<ShellContentCreatorInfo> getCodec() {
        return CODEC;
    }
}

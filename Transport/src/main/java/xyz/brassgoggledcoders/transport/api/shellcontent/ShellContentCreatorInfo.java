package xyz.brassgoggledcoders.transport.api.shellcontent;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.codec.Codecs;

import javax.annotation.Nullable;
import java.util.Optional;

public record ShellContentCreatorInfo(
        ResourceLocation id,
        BlockState viewState,
        Component name,
        boolean createRecipe,
        IShellContentCreator<?> contentCreator
) {
    private static final Lazy<Codec<ShellContentCreatorInfo>> CODEC = Lazy.of(() ->
            RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ShellContentCreatorInfo::id),
                    BlockState.CODEC.fieldOf("view_state")
                            .forGetter(ShellContentCreatorInfo::viewState),
                    Codecs.COMPONENT.optionalFieldOf("name")
                            .forGetter(creatorInfo -> Optional.of(creatorInfo.name())),
                    Codec.BOOL.optionalFieldOf("createRecipe", Boolean.TRUE)
                            .forGetter(ShellContentCreatorInfo::createRecipe),
                    RegistryManager.ACTIVE.getRegistry(TransportAPI.SHELL_CONTENT_TYPE_KEY)
                            .getCodec()
                            .<IShellContentCreator<?>>dispatch(IShellContentCreator::getType, ShellContentType::getCodec)
                            .fieldOf("content")
                            .forGetter(ShellContentCreatorInfo::contentCreator)
            ).apply(instance, (id, viewState, name, createRecipe, content) -> new ShellContentCreatorInfo(
                    id,
                    viewState,
                    name.orElseGet(() -> viewState.getBlock().getName()),
                    createRecipe,
                    content)
            ))
    );

    public static final String NBT_TAG_ELEMENT = "shellContent";

    public ShellContent create(@Nullable CompoundTag nbt) {
        ShellContent shellContent = this.contentCreator().get();
        shellContent.setCreatorInfo(this);
        if (nbt != null && nbt.size() > 0) {
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
        itemStack.getOrCreateTagElement(NBT_TAG_ELEMENT)
                .putString("id", this.id().toString());
        return itemStack;
    }

    public static ShellContentCreatorInfo fromTag(CompoundTag tag) {
        return ShellContentCreatorInfo.getCodec().decode(NbtOps.INSTANCE, tag)
                .resultOrPartial(error -> TransportAPI.LOGGER.warn("Failed to decode Creator info. Error {}, Tag {}", error, tag))
                .map(Pair::getFirst)
                .orElseGet(TransportAPI.SHELL_CONTENT_CREATOR.get()::getEmpty);
    }

    public static Codec<ShellContentCreatorInfo> getCodec() {
        return CODEC.get();
    }
}

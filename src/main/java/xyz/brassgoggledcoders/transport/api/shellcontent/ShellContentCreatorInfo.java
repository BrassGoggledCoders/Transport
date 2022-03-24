package xyz.brassgoggledcoders.transport.api.shellcontent;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.content.TransportShellContentTypes;

import javax.annotation.Nullable;
import java.util.Optional;

public record ShellContentCreatorInfo(
        ResourceLocation id,
        BlockState blockState,
        boolean createRecipe,
        IShellContentCreator<?> contentCreator
) {
    public static final Codec<ShellContentCreatorInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(ShellContentCreatorInfo::id),
            BlockState.CODEC.fieldOf("blockState")
                    .forGetter(ShellContentCreatorInfo::blockState),
            Codec.BOOL.optionalFieldOf("createRecipe", Boolean.TRUE)
                    .forGetter(ShellContentCreatorInfo::createRecipe),
            TransportShellContentTypes.SHELL_CONTENT_TYPES.get()
                    .getCodec()
                    .<IShellContentCreator<?>>dispatch(IShellContentCreator::getType, ShellContentType::getCodec)
                    .fieldOf("content")
                    .forGetter(ShellContentCreatorInfo::contentCreator)
    ).apply(instance, ShellContentCreatorInfo::new));

    public ShellContent create(@Nullable CompoundTag nbt) {
        ShellContent shellContent = this.contentCreator().get();
        shellContent.setCreatorInfo(this);
        if (nbt != null) {
            shellContent.deserializeNBT(nbt);
        }
        return shellContent;
    }

    public Optional<CompoundTag> asTag() {
        return ShellContentCreatorInfo.CODEC
                .encode(this, NbtOps.INSTANCE, NbtOps.INSTANCE.empty())
                .result()
                .filter(CompoundTag.class::isInstance)
                .map(CompoundTag.class::cast);
    }

    public static ShellContentCreatorInfo fromTag(CompoundTag tag) {
        return ShellContentCreatorInfo.CODEC.decode(NbtOps.INSTANCE, tag)
                .resultOrPartial(error -> TransportAPI.LOGGER.warn("Failed to decode Creator info. Error: {}, Tag{}", error, tag))
                .map(Pair::getFirst)
                .orElseGet(TransportAPI.SHELL_CONTENT_CREATOR.get()::getEmpty);
    }
}

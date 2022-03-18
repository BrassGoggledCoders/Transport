package xyz.brassgoggledcoders.transport.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.content.TransportShellContentTypes;

public record ShellContentCreatorInfo(
        BlockState blockState,
        boolean createRecipe,
        IShellContentCreator<?> contentCreator
) {
    public static final Codec<ShellContentCreatorInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
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
}

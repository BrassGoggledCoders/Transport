package xyz.brassgoggledcoders.transport.shellcontent.empty;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;

public class EmptyShellContentCreator implements IShellContentCreator<EmptyShellContent> {
    public static final Codec<EmptyShellContentCreator> CODEC = Codec.unit(EmptyShellContentCreator::new);

    @Override
    public Codec<? extends IShellContentCreator<?>> getCodec() {
        return CODEC;
    }

    @NotNull
    @Override
    public EmptyShellContent get() {
        return new EmptyShellContent();
    }
}

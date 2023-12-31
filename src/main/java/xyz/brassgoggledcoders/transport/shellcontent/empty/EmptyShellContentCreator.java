package xyz.brassgoggledcoders.transport.shellcontent.empty;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;

public class EmptyShellContentCreator implements IShellContentCreator<EmptyShellContent> {
    public static final EmptyShellContentCreator INSTANCE = new EmptyShellContentCreator();
    public static final Codec<EmptyShellContentCreator> CODEC = Codec.unit(INSTANCE);

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

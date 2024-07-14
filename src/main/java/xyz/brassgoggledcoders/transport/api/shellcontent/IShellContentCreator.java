package xyz.brassgoggledcoders.transport.api.shellcontent;

import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public interface IShellContentCreator<U extends ShellContent> extends Supplier<U> {

    Codec<? extends IShellContentCreator<?>> getCodec();
}

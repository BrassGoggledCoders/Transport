package xyz.brassgoggledcoders.transport.api.shellcontent;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.util.NonNullSupplier;

public interface IShellContentCreator<U extends ShellContent> extends NonNullSupplier<U> {

    Codec<? extends IShellContentCreator<?>> getCodec();
}

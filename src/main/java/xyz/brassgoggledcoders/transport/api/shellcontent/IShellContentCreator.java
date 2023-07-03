package xyz.brassgoggledcoders.transport.api.shellcontent;

import net.minecraftforge.common.util.NonNullSupplier;

public interface IShellContentCreator<U extends ShellContent> extends NonNullSupplier<U> {

    ShellContentType<?> getType();
}

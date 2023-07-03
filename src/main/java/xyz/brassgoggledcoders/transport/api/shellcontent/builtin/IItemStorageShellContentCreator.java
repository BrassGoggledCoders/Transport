package xyz.brassgoggledcoders.transport.api.shellcontent.builtin;

import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public interface IItemStorageShellContentCreator<T extends ShellContent> extends IShellContentCreator<T> {
    StorageSize getSize();

    boolean isShowScreen();
}

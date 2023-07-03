package xyz.brassgoggledcoders.transport.api.shellcontent.builtin;

import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public interface IFluidStorageShellContentCreator<T extends ShellContent> extends IShellContentCreator<T> {
    int getCapacity();

    boolean isAllowItemInteraction();
}

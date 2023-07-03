package xyz.brassgoggledcoders.transport.api.shellcontent.builtin;

import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public interface IEnergyStorageShellContentCreator<T extends ShellContent> extends IShellContentCreator<T> {
    int getCapacity();

    int getMaxReceive();

    int getMaxExtract();

    boolean isCreative();
}

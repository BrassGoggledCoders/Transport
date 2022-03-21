package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;

public class ClientShellContentHolder implements IShellContentHolder {
    private ShellContent shellContent;

    @Override
    public void update(ShellContent shellContent) {
        this.shellContent = shellContent;
    }

    @Nonnull
    @Override
    public ShellContent get() {
        return this.shellContent;
    }
}

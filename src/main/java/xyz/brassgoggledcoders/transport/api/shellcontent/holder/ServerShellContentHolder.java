package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.service.IShellContentCreatorService;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;

public class ServerShellContentHolder implements IShellContentHolder {
    private final IShellContentCreatorService manager;
    private final IShell shell;
    private ShellContent shellContent;
    private int generation;

    public ServerShellContentHolder(IShellContentCreatorService manager, IShell shell) {
        this.manager = manager;
        this.shell = shell;
    }

    private void checkGeneration() {
        if (this.generation != manager.getGeneration()) {
            CompoundTag nbt = null;
            ShellContentCreatorInfo info = null;
            if (this.shellContent != null) {
                nbt = this.shellContent.serializeNBT();
                info = this.shellContent.getCreatorInfo();
                this.shellContent.invalidateCaps();
            }

            this.shellContent = this.manager.create(info != null ? info.id() : Transport.rl("missing"), nbt);
            this.shellContent.setShell(this.shell);

            this.generation = this.manager.getGeneration();
        }
    }

    @Override
    @Nonnull
    public ShellContent get() {
        checkGeneration();

        return this.shellContent;
    }

    @Override
    public void update(ShellContent shellContent) {
        this.shellContent = shellContent;
    }
}

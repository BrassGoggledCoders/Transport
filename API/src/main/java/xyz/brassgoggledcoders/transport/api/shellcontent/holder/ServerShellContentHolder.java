package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.service.IShellContentCreatorService;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ServerShellContentHolder implements IShellContentHolder {
    private final IShellContentCreatorService manager;
    private final IShell shell;
    private ShellContent shellContent;
    private int generation;
    private Component name;

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

            ResourceLocation id = info != null ? info.id() : this.manager.getEmpty().id();
            this.shellContent = this.manager.create(id, nbt);
            this.shellContent.setShell(this.shell);
            this.name = this.shell.getWithName(this.shellContent);

            this.generation = this.manager.getGeneration();
            this.shell.newGeneration();
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
        this.generation = this.manager.getGeneration();
        this.shellContent = shellContent;
        this.shellContent.setShell(this.shell);
        this.name = this.shell.getWithName(this.shellContent);
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf byteBuf) {
        Optional<CompoundTag> tag = this.get().getCreatorInfo().asTag();
        byteBuf.writeBoolean(tag.isPresent());
        tag.ifPresent(byteBuf::writeNbt);
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf byteBuf) {

    }

    @Override
    @Nonnull
    public Component getName() {
        checkGeneration();

        return this.name;
    }
}

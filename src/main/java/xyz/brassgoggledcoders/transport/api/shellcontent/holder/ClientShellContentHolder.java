package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;

public class ClientShellContentHolder implements IShellContentHolder {
    private final IShell shell;
    private ShellContent shellContent;
    private Component name;

    public ClientShellContentHolder(IShell shell) {
        this.shell = shell;
    }

    @Override
    public void update(ShellContent shellContent) {
        this.shellContent = shellContent;
        this.shellContent.setShell(shell);
        this.name = shell.getWithName(shellContent);
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf byteBuf) {

    }

    @Override
    public void readFromBuffer(FriendlyByteBuf byteBuf) {
        if (byteBuf.readBoolean()) {
            this.update(ShellContentCreatorInfo.fromTag(byteBuf.readNbt())
                    .create(null)
            );
        }
    }

    @Override
    @Nonnull
    public Component getName() {
        checkNonnull();
        if (this.name == null) {
            return Component.literal("WTF");
        }
        return this.name;
    }

    @Override
    public ItemStack asItemStack() {
        ItemStack itemStack = this.shell.asItemStack();
        TransportAPI.SHELL_CONTENT_CREATOR.get()
                .writeData(this.shellContent, itemStack.getOrCreateTag());
        return itemStack;
    }

    private void checkNonnull() {
        if (shellContent == null) {
            this.shellContent = TransportAPI.SHELL_CONTENT_CREATOR.get()
                    .getEmpty()
                    .create(null);
            this.shellContent.setShell(this.shell);
        }
    }

    @Nonnull
    @Override
    public ShellContent get() {
        checkNonnull();
        return this.shellContent;
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (shellContent == null || shellContent.getCreatorInfo() == TransportAPI.SHELL_CONTENT_CREATOR.get().getEmpty()) {
            this.update(TransportAPI.SHELL_CONTENT_CREATOR.get()
                    .create(nbt)
            );
        }
    }
}

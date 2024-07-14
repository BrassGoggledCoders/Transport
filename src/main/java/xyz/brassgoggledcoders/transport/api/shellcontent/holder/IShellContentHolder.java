package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface IShellContentHolder extends Supplier<ShellContent> {
    static IShellContentHolder createForSide(IShell shell) {
        if (shell.getShellLevel().isClientSide()) {
            return new ClientShellContentHolder(shell);
        } else {
            return new ServerShellContentHolder(TransportAPI.SHELL_CONTENT_CREATOR.get(), shell);
        }
    }

    void update(ShellContent shellContent);

    void writeToBuffer(FriendlyByteBuf byteBuf);

    void readFromBuffer(FriendlyByteBuf byteBuf);

    @Nonnull
    Component getName();

    ItemStack asItemStack();

    void save(CompoundTag tag);

    void load(CompoundTag tag);
}

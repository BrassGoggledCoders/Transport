package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;

public interface IShellContentHolder extends NonNullSupplier<ShellContent>, INBTSerializable<CompoundTag> {
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
}

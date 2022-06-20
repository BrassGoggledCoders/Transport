package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;

public interface IShellContentHolder extends NonNullSupplier<ShellContent> {
    void update(ShellContent shellContent);

    void writeToBuffer(FriendlyByteBuf byteBuf);

    void readFromBuffer(FriendlyByteBuf byteBuf);

    @Nonnull
    Component getName();

    ItemStack asItemStack();
}

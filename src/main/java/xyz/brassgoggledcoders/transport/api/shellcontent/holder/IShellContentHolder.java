package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public interface IShellContentHolder extends NonNullSupplier<ShellContent> {
    void update(ShellContent shellContent);

    void writeToBuffer(FriendlyByteBuf byteBuf);

    void readFromBuffer(FriendlyByteBuf byteBuf);
}

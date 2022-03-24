package xyz.brassgoggledcoders.transport.api.shellcontent.holder;

import net.minecraft.network.FriendlyByteBuf;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;

import javax.annotation.Nonnull;

public class ClientShellContentHolder implements IShellContentHolder {
    private ShellContent shellContent;

    @Override
    public void update(ShellContent shellContent) {
        this.shellContent = shellContent;
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

    private void checkNonnull() {
        if (shellContent == null) {
            this.shellContent = TransportAPI.SHELL_CONTENT_CREATOR.get()
                    .getEmpty()
                    .create(null);
        }
    }

    @Nonnull
    @Override
    public ShellContent get() {
        checkNonnull();
        return this.shellContent;
    }
}

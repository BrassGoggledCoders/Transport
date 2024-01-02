package xyz.brassgoggledcoders.transport.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.service.ShellContentCreatorServiceImpl;

import java.util.Collection;
import java.util.function.Supplier;

public record SyncShellContentCreatorInfoMessage(
        Collection<ShellContentCreatorInfo> shellContentCreatorInfos
) {

    void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeCollection(
                this.shellContentCreatorInfos(),
                (listByteBuffer, shellContentCreatorInfo) -> shellContentCreatorInfo.asTag()
                        .ifPresent(listByteBuffer::writeNbt)
        );
    }

    void consume(Supplier<NetworkEvent.Context> ignoredContextSupplier) {
        if (TransportAPI.SHELL_CONTENT_CREATOR.get() instanceof ShellContentCreatorServiceImpl impl) {
            impl.updateClient(this.shellContentCreatorInfos());
        }
    }

    public static SyncShellContentCreatorInfoMessage decode(FriendlyByteBuf friendlyByteBuf) {
        return new SyncShellContentCreatorInfoMessage(friendlyByteBuf.readList((listByteBuf) ->
                ShellContentCreatorInfo.fromTag(listByteBuf.readNbt())
        ));
    }
}

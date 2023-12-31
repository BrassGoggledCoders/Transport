package xyz.brassgoggledcoders.transport.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.service.ShellContentCreatorServiceImpl;
import xyz.brassgoggledcoders.transport.shellcontent.empty.EmptyShellContentCreator;

import java.util.Collection;
import java.util.function.Supplier;

public record SyncShellContentCreatorInfoMessage(
        Collection<ShellContentCreatorInfo> shellContentCreatorInfos
) {

    void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeCollection(
                this.shellContentCreatorInfos(),
                (listByteBuffer, shellContentCreatorInfo) -> {
                    listByteBuffer.writeResourceLocation(shellContentCreatorInfo.id());
                    listByteBuffer.writeInt(Block.getId(shellContentCreatorInfo.viewState()));
                    listByteBuffer.writeComponent(shellContentCreatorInfo.name());
                    listByteBuffer.writeBoolean(shellContentCreatorInfo.createRecipe());
                }
        );
    }

    void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        if (TransportAPI.SHELL_CONTENT_CREATOR.get() instanceof ShellContentCreatorServiceImpl impl) {
            impl.updateClient(this.shellContentCreatorInfos());
        }
    }

    public static SyncShellContentCreatorInfoMessage decode(FriendlyByteBuf friendlyByteBuf) {
        return new SyncShellContentCreatorInfoMessage(friendlyByteBuf.readList((listByteBuf) -> new ShellContentCreatorInfo(
                listByteBuf.readResourceLocation(),
                Block.stateById(listByteBuf.readInt()),
                listByteBuf.readComponent(),
                listByteBuf.readBoolean(),
                EmptyShellContentCreator.INSTANCE
        )));
    }
}

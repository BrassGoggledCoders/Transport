package xyz.brassgoggledcoders.transport.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.service.ShellContentCreatorServiceImpl;

import java.util.Map;

public record SyncShellContentCreatorInfoMessage(
        Map<ResourceLocation, ShellContentCreatorInfo> shellContentCreatorInfos
) implements CustomPacketPayload {
    public static final ResourceLocation ID = Transport.rl("sync_shell_content_creator_info");

    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeMap(
                this.shellContentCreatorInfos(),
                FriendlyByteBuf::writeResourceLocation,
                (valueByteBuf, shellContentCreatorInfo) -> shellContentCreatorInfo.asTag()
                        .ifPresent(valueByteBuf::writeNbt)
        );
    }

    @Override
    @NotNull
    public ResourceLocation id() {
        return ID;
    }

    public static SyncShellContentCreatorInfoMessage decode(FriendlyByteBuf friendlyByteBuf) {
        return new SyncShellContentCreatorInfoMessage(friendlyByteBuf.readMap(
                FriendlyByteBuf::readResourceLocation,
                (valueByteBuff) -> ShellContentCreatorInfo.fromTag(valueByteBuff.readNbt())
        ));
    }

    public static void consume(SyncShellContentCreatorInfoMessage message, IPayloadContext context) {
        context.workHandler()
                .execute(() -> {
                    if (TransportAPI.SHELL_CONTENT_CREATOR.get() instanceof ShellContentCreatorServiceImpl impl) {
                        impl.updateClient(message.shellContentCreatorInfos());
                    }
                });
    }
}

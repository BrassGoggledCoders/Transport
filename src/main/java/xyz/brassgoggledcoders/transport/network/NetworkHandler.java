package xyz.brassgoggledcoders.transport.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

public class NetworkHandler {
    private static final String VERSION = "1.1.0";
    private final SimpleChannel channel;

    public NetworkHandler() {
        this.channel = NetworkRegistry.ChannelBuilder.named(Transport.rl("main"))
                .networkProtocolVersion(() -> VERSION)
                .clientAcceptedVersions(VERSION::equalsIgnoreCase)
                .serverAcceptedVersions(VERSION::equalsIgnoreCase)
                .simpleChannel();

        this.channel.messageBuilder(NewGenerationClientMessage.class, 0)
                .decoder(NewGenerationClientMessage::decode)
                .encoder(NewGenerationClientMessage::encode)
                .consumerMainThread(NewGenerationClientMessage::consume)
                .add();

        this.channel.messageBuilder(OpenMenuProviderServerMessage.class, 1)
                .decoder(friendlyByteBuf -> new OpenMenuProviderServerMessage())
                .encoder(((openMenuProviderServerMessage, friendlyByteBuf) -> {
                }))
                .consumerMainThread(OpenMenuProviderServerMessage::consume)
                .add();

        this.channel.messageBuilder(SyncShellContentCreatorInfoMessage.class, 2)
                .decoder(SyncShellContentCreatorInfoMessage::decode)
                .encoder(SyncShellContentCreatorInfoMessage::encode)
                .consumerMainThread(SyncShellContentCreatorInfoMessage::consume)
                .add();
    }

    public void sendNewGenerationMessage(IShell shell) {
        this.channel.send(
                PacketDistributor.TRACKING_ENTITY.with(shell::getSelf),
                new NewGenerationClientMessage(
                        shell.getShellId(),
                        shell.getHolder()
                                .get()
                                .getCreatorInfo()
                )
        );
    }

    public void sendOpenMenuProvider() {
        this.channel.send(
                PacketDistributor.SERVER.noArg(),
                new OpenMenuProviderServerMessage()
        );
    }

    public void sendSyncShellContentInfoMessage(@Nullable ServerPlayer player) {
        PacketTarget packetTarget;
        if (player != null) {
            packetTarget = PacketDistributor.PLAYER.with(() -> player);
        } else {
            packetTarget = PacketDistributor.ALL.noArg();
        }
        this.channel.send(
                packetTarget,
                new SyncShellContentCreatorInfoMessage(TransportAPI.SHELL_CONTENT_CREATOR.get()
                        .getAll()
                )
        );
    }
}

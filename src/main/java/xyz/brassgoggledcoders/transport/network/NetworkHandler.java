package xyz.brassgoggledcoders.transport.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

public class NetworkHandler {
    private static final String VERSION = "1.0.0";
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
}

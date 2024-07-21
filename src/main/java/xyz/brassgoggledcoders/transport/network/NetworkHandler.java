package xyz.brassgoggledcoders.transport.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

public class NetworkHandler {
    public NetworkHandler() {

    }

    public void sendNewGenerationMessage(IShell shell) {
        PacketDistributor.TRACKING_ENTITY.with(shell.getSelf())
                .send(new NewGenerationClientMessage(
                        shell.getShellId(),
                        shell.getHolder()
                                .get()
                                .getCreatorInfo()
                ));
    }

    public void sendOpenMenuProvider() {
        PacketDistributor.SERVER.noArg()
                .send(new OpenMenuProviderServerMessage());
    }

    public void sendSyncShellContentInfoMessage(@Nullable ServerPlayer player) {
        PacketDistributor.PacketTarget packetTarget;
        if (player != null) {
            packetTarget = PacketDistributor.PLAYER.with(player);
        } else {
            packetTarget = PacketDistributor.ALL.noArg();
        }
        packetTarget.send(
                new SyncShellContentCreatorInfoMessage(TransportAPI.SHELL_CONTENT_CREATOR.get().getMap())
        );
    }
}

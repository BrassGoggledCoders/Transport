package xyz.brassgoggledcoders.transport.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

import java.util.function.Supplier;

public class OpenMenuProviderServerMessage {
    public void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer serverPlayer = contextSupplier.get().getSender();
        if (serverPlayer != null && serverPlayer.getVehicle() instanceof IShell shell) {
            if (shell.getContent() instanceof MenuProvider menuProvider) {
                serverPlayer.openMenu(menuProvider);
            }
        }
    }
}

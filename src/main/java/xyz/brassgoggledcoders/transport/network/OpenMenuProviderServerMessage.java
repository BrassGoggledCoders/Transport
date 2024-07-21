package xyz.brassgoggledcoders.transport.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

public class OpenMenuProviderServerMessage implements CustomPacketPayload {
    public static final ResourceLocation ID = Transport.rl("open_menu");

    @Override
    public void write(@NotNull FriendlyByteBuf p_294947_) {

    }

    @Override
    @NotNull
    public ResourceLocation id() {
        return ID;
    }

    public static void consume(OpenMenuProviderServerMessage ignoredMenuProviderServerMessage, IPayloadContext context) {
        context.workHandler()
                .execute(() -> context.player()
                        .ifPresent(player -> {
                            if (player instanceof ServerPlayer serverPlayer && serverPlayer.getVehicle() instanceof IShell shell) {
                                if (shell.getContent() instanceof MenuProvider menuProvider) {
                                    serverPlayer.openMenu(menuProvider);
                                }
                            }
                        })
                );


    }
}

package xyz.brassgoggledcoders.transport.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.network.INetworkHandler;

public class NetworkHandler implements INetworkHandler {
    private static final String VERSION = "1";
    private final SimpleChannel channel;

    public NetworkHandler() {
        this.channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Transport.ID, "network_handler"),
                () -> VERSION,
                VERSION::equals,
                VERSION::equals
        );

        this.channel.messageBuilder(UpdateModuleCaseMessage.class, 0)
                .decoder(UpdateModuleCaseMessage::decode)
                .encoder(UpdateModuleCaseMessage::encode)
                .consumer(UpdateModuleCaseMessage::consume)
                .add();
    }

    @Override
    public void sendModuleCaseUpdate(IModularEntity entity, ModuleInstance<?> changed, boolean added) {
        if (!entity.getTheWorld().isRemote()) {
            this.channel.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity::getSelf),
                    new UpdateModuleCaseMessage(
                            entity,
                            changed,
                            added
                    )
            );
        }
    }
}

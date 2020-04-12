package xyz.brassgoggledcoders.transport.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlot;
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

        this.channel.messageBuilder(AddModuleCaseMessage.class, 0)
                .decoder(AddModuleCaseMessage::decode)
                .encoder(AddModuleCaseMessage::encode)
                .consumer(AddModuleCaseMessage::consume)
                .add();
    }

    @Override
    public void sendAddModuleCase(IModularEntity entity, ModuleInstance<?> moduleInstance, ModuleSlot moduleSlot) {
        if (!entity.getTheWorld().isRemote()) {
            this.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity::getSelf),
                    new AddModuleCaseMessage(entity, moduleInstance, moduleSlot));
        }
    }
}

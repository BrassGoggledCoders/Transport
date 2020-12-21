package xyz.brassgoggledcoders.transport.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.network.INetworkHandler;

import javax.annotation.Nullable;

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

        this.channel.messageBuilder(ModifyModuleCaseMessage.class, 0)
                .decoder(ModifyModuleCaseMessage::decode)
                .encoder(ModifyModuleCaseMessage::encode)
                .consumer(ModifyModuleCaseMessage::consume)
                .add();

        this.channel.messageBuilder(UpdateModuleInstanceMessage.class, 1)
                .decoder(UpdateModuleInstanceMessage::decode)
                .encoder(UpdateModuleInstanceMessage::encode)
                .consumer(UpdateModuleInstanceMessage::consume)
                .add();
    }

    @Override
    public void sendModifyModuleCase(IModularEntity entity, ModuleInstance<?> moduleInstance, ModuleSlot moduleSlot,
                                     boolean add) {
        if (!entity.getTheWorld().isRemote()) {
            this.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity::getSelf),
                    new ModifyModuleCaseMessage(entity, moduleInstance, moduleSlot, add));
        }
    }

    @Override
    public void sendModuleInstanceUpdate(IModularEntity entity, ModuleSlot moduleSlot, int type, @Nullable CompoundNBT updateInfo) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity::getSelf),
                new UpdateModuleInstanceMessage(entity.getSelf().getEntityId(), moduleSlot, type, updateInfo));
    }
}

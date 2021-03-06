package xyz.brassgoggledcoders.transport.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.ModuleTab;
import xyz.brassgoggledcoders.transport.api.network.INetworkHandler;
import xyz.brassgoggledcoders.transport.network.property.UpdateClientContainerPropertiesMessage;
import xyz.brassgoggledcoders.transport.network.property.UpdateServerContainerPropertyMessage;

import javax.annotation.Nullable;
import java.util.UUID;

public class NetworkHandler implements INetworkHandler {
    private static final String VERSION = "2";
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

        this.channel.messageBuilder(UpdateModuleInstanceMessage.class, 1)
                .decoder(UpdateModuleInstanceMessage::decode)
                .encoder(UpdateModuleInstanceMessage::encode)
                .consumer(UpdateModuleInstanceMessage::consume)
                .add();

        this.channel.messageBuilder(UpdateClientContainerPropertiesMessage.class, 2)
                .decoder(UpdateClientContainerPropertiesMessage::decode)
                .encoder(UpdateClientContainerPropertiesMessage::encode)
                .consumer(UpdateClientContainerPropertiesMessage::consume)
                .add();

        this.channel.messageBuilder(UpdateServerContainerPropertyMessage.class, 3)
                .decoder(UpdateServerContainerPropertyMessage::decode)
                .encoder(UpdateServerContainerPropertyMessage::encode)
                .consumer(UpdateServerContainerPropertyMessage::consume)
                .add();

        this.channel.messageBuilder(UpdateModuleScreenInfoMessage.class, 4)
                .decoder(UpdateModuleScreenInfoMessage::decode)
                .encoder(UpdateModuleScreenInfoMessage::encode)
                .consumer(UpdateModuleScreenInfoMessage::consume)
                .add();

        this.channel.messageBuilder(ModuleTabClickedMessage.class, 5)
                .decoder(ModuleTabClickedMessage::decode)
                .encoder(ModuleTabClickedMessage::encode)
                .consumer(ModuleTabClickedMessage::consume)
                .add();
    }

    @Override
    public void sendAddModuleCase(IModularEntity entity, ModuleInstance<?> moduleInstance, ModuleSlot moduleSlot) {
        if (!entity.getTheWorld().isRemote()) {
            this.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity::getSelf),
                    new AddModuleCaseMessage(entity, moduleInstance, moduleSlot));
        }
    }

    @Override
    public void sendModuleInstanceUpdate(IModularEntity entity, ModuleSlot moduleSlot, int type, @Nullable CompoundNBT updateInfo) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity::getSelf),
                new UpdateModuleInstanceMessage(entity.getSelf().getEntityId(), moduleSlot, type, updateInfo));
    }

    @Override
    public void sendModularScreenInfo(IModularEntity entity, UUID uniqueId, Container container) {
        this.channel.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity::getSelf),
                new UpdateModuleScreenInfoMessage(
                        (short) container.windowId,
                        container.getType(),
                        entity.getSelf()
                                .getEntityId(),
                        uniqueId,
                        entity.getModuleTabs()
                ));
    }

    public void sendTabClickedMessage(int entityId, ModuleTab moduleTab) {
        this.channel.send(
                PacketDistributor.SERVER.noArg(),
                new ModuleTabClickedMessage(
                        entityId,
                        moduleTab.getUniqueId()
                )
        );
    }

    public void sendUpdateClientContainerProperties(ServerPlayerEntity playerEntity, UpdateClientContainerPropertiesMessage message) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> playerEntity), message);
    }

    public void sendUpdateServerContainerProperties(UpdateServerContainerPropertyMessage message) {
        this.channel.send(PacketDistributor.SERVER.noArg(), message);
    }
}

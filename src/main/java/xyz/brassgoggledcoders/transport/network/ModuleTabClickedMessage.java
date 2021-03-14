package xyz.brassgoggledcoders.transport.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

import java.util.UUID;
import java.util.function.Supplier;

public class ModuleTabClickedMessage {
    private final int entityId;
    private final UUID uniqueId;

    public ModuleTabClickedMessage(int entityId, UUID uniqueId) {
        this.entityId = entityId;
        this.uniqueId = uniqueId;
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(entityId);
        packetBuffer.writeUniqueId(uniqueId);
    }

    public boolean consume(Supplier<Context> contextSupplier) {
        Context context = contextSupplier.get();
        context.setPacketHandled(true);
        context.enqueueWork(() -> {
            ServerPlayerEntity serverPlayerEntity = context.getSender();
            if (serverPlayerEntity != null) {
                Entity entity = serverPlayerEntity.getEntityWorld()
                        .getEntityByID(entityId);
                if (entity != null) {
                    if (entity.getUniqueID().equals(uniqueId)) {
                        entity.getCapability(TransportAPI.MODULAR_ENTITY)
                        .ifPresent(modularEntity -> modularEntity.onTabClicked(serverPlayerEntity));
                    } else {
                        entity.getCapability(TransportAPI.MODULAR_ENTITY)
                                .<ModuleInstance<?>>map(modularEntity -> modularEntity.getModuleInstance(uniqueId))
                                .ifPresent(moduleInstance -> moduleInstance.onTabClicked(serverPlayerEntity));
                    }
                }
            }
        });
        return true;
    }

    public static ModuleTabClickedMessage decode(PacketBuffer packetBuffer) {
        return new ModuleTabClickedMessage(
                packetBuffer.readInt(),
                packetBuffer.readUniqueId()
        );
    }
}

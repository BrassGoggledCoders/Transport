package xyz.brassgoggledcoders.transport.network;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.ClientEventHandler;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

import java.util.UUID;
import java.util.function.Supplier;

public class UpdateModuleCaseMessage {
    private final int entityId;
    private final Module<?> module;
    private final UUID uniqueId;
    private final CompoundNBT moduleInstanceNBT;
    private final boolean added;

    public UpdateModuleCaseMessage(IModularEntity modularEntity, ModuleInstance<?> moduleInstance, boolean added) {
        this(
                modularEntity.getSelf().getEntityId(),
                moduleInstance.getModule(),
                moduleInstance.getUniqueId(),
                moduleInstance.serializeNBT(),
                added
        );
    }

    public UpdateModuleCaseMessage(int entityId, Module<?> module, UUID uniqueId, CompoundNBT moduleInstanceNBT, boolean added) {
        this.entityId = entityId;
        this.module = module;
        this.uniqueId = uniqueId;
        this.moduleInstanceNBT = moduleInstanceNBT;
        this.added = added;

    }

    public static UpdateModuleCaseMessage decode(PacketBuffer packetBuffer) {
        return new UpdateModuleCaseMessage(
                packetBuffer.readInt(),
                Module.fromPacketBuffer(packetBuffer),
                packetBuffer.readUniqueId(),
                new CompoundNBT(),
                packetBuffer.readBoolean()
        );
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(entityId);
        Module.toPacketBuffer(module, packetBuffer);
        packetBuffer.writeUniqueId(uniqueId);
        packetBuffer.writeBoolean(added);
    }

    public void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            Entity entity = ClientEventHandler.getWorld().getEntityByID(entityId);
            if (entity instanceof IModularEntity && module != null) {
                if (added) {
                    ModuleInstance<?> moduleInstance = ((IModularEntity) entity).getModuleCase().addModule(module, false);
                    if (moduleInstance != null) {
                        moduleInstance.deserializeNBT(moduleInstanceNBT);
                    }
                } else {
                    ((IModularEntity) entity).getModuleCase().removeByUniqueId(uniqueId, false);
                }
            }
        }));
        contextSupplier.get().setPacketHandled(true);
    }
}

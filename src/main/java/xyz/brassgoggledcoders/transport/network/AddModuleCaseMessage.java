package xyz.brassgoggledcoders.transport.network;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.ClientEventHandler;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlots;

import java.util.function.Supplier;

public class AddModuleCaseMessage {
    private final int entityId;
    private final Module<?> module;
    private final ModuleSlot moduleSlot;

    public AddModuleCaseMessage(IModularEntity modularEntity, ModuleInstance<?> moduleInstance, ModuleSlot moduleSlot) {
        this(modularEntity.getSelf().getEntityId(), moduleInstance.getModule(), moduleSlot);
    }

    public AddModuleCaseMessage(int entityId, Module<?> module, ModuleSlot moduleSlot) {
        this.entityId = entityId;
        this.module = module;
        this.moduleSlot = moduleSlot;
    }

    public static AddModuleCaseMessage decode(PacketBuffer packetBuffer) {
        return new AddModuleCaseMessage(packetBuffer.readInt(), Module.fromPacketBuffer(packetBuffer),
                ModuleSlots.MODULE_SLOT_MAP.get(packetBuffer.readString(64)));
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(entityId);
        Module.toPacketBuffer(module, packetBuffer);
        packetBuffer.writeString(moduleSlot.getName());
    }

    public void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            Entity entity = ClientEventHandler.getWorld().getEntityByID(entityId);
            if (entity instanceof IModularEntity && module != null && moduleSlot != null) {
                ((IModularEntity) entity).getModuleCase().addModule(module, moduleSlot, false);
            }
        }));
        contextSupplier.get().setPacketHandled(true);
    }
}

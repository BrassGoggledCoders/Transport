package xyz.brassgoggledcoders.transport.network;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;

import java.util.Objects;
import java.util.Optional;
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
                TransportAPI.getModuleSlot(packetBuffer.readResourceLocation()));
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(entityId);
        Module.toPacketBuffer(module, packetBuffer);
        packetBuffer.writeResourceLocation(Objects.requireNonNull(moduleSlot.getRegistryName()));
    }

    public void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        LogicalSide side = contextSupplier.get().getDirection().getReceptionSide();
        contextSupplier.get().enqueueWork(() -> {
            if (module != null && moduleSlot != null) {
                Optional<Entity> entity = LogicalSidedProvider.CLIENTWORLD.<Optional<World>>get(side)
                    .map(world -> world.getEntityByID(entityId));
                entity.ifPresent(value -> value.getCapability(TransportAPI.MODULAR_ENTITY)
                        .ifPresent(modularEntity -> modularEntity.add(module, moduleSlot, false)));
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}

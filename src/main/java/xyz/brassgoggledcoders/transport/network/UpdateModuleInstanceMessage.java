package xyz.brassgoggledcoders.transport.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class UpdateModuleInstanceMessage {
    private final int entityId;
    private final ModuleSlot moduleSlot;
    private final int type;
    private final CompoundNBT compoundNBT;

    public UpdateModuleInstanceMessage(int entityId, ModuleSlot moduleSlot, int type, @Nullable CompoundNBT compoundNBT) {
        this.entityId = entityId;
        this.moduleSlot = moduleSlot;
        this.type = type;
        this.compoundNBT = compoundNBT;
    }

    public static UpdateModuleInstanceMessage decode(PacketBuffer packetBuffer) {
        return new UpdateModuleInstanceMessage(
                packetBuffer.readInt(),
                packetBuffer.readRegistryId(),
                packetBuffer.readInt(),
                packetBuffer.readBoolean() ? packetBuffer.readCompoundTag() : null
        );
    }

    public static void encode(UpdateModuleInstanceMessage updateModuleInstanceMessage, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(updateModuleInstanceMessage.entityId);
        packetBuffer.writeRegistryId(updateModuleInstanceMessage.moduleSlot);
        packetBuffer.writeBoolean(updateModuleInstanceMessage.compoundNBT != null);
        if (updateModuleInstanceMessage.compoundNBT != null) {
            packetBuffer.writeCompoundTag(updateModuleInstanceMessage.compoundNBT);
        }
    }

    public static boolean consume(UpdateModuleInstanceMessage updateModuleInstanceMessage,
                                  Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            LogicalSide side = contextSupplier.get().getDirection().getReceptionSide();
            LogicalSidedProvider.CLIENTWORLD.<Optional<World>>get(side)
                    .map(world -> world.getEntityByID(updateModuleInstanceMessage.entityId))
                    .ifPresent(entity -> entity.getCapability(TransportAPI.MODULAR_ENTITY)
                            .ifPresent(modularEntity -> {
                                ModuleInstance<?> moduleInstance = modularEntity.getModuleInstance(
                                        updateModuleInstanceMessage.moduleSlot);
                                if (moduleInstance != null) {
                                    moduleInstance.receiveClientUpdate(updateModuleInstanceMessage.type,
                                            updateModuleInstanceMessage.compoundNBT);
                                }
                            }));
        });
        return true;
    }
}

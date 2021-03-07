package xyz.brassgoggledcoders.transport.network;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleTab;
import xyz.brassgoggledcoders.transport.screen.ModularScreenInfo;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UpdateModuleScreenInfoMessage {
    private final short id;
    private final ContainerType<?> type;
    private final UUID picked;
    private final List<ModuleTab> moduleTabList;

    public UpdateModuleScreenInfoMessage(short id, ContainerType<?> type, UUID picked, List<ModuleTab> moduleTabList) {
        this.id = id;
        this.type = type;
        this.picked = picked;
        this.moduleTabList = moduleTabList;
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeShort(id);
        packetBuffer.writeRegistryId(type);
        packetBuffer.writeUniqueId(picked);
        packetBuffer.writeShort(moduleTabList.size());
        for (ModuleTab moduleTab : moduleTabList) {
            moduleTab.toPacketBuffer(packetBuffer);
        }
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> ModularScreenInfo.setCurrent(new ModularScreenInfo(
                id,
                type,
                picked,
                moduleTabList
        )));
        contextSupplier.get().setPacketHandled(true);
        return true;
    }

    public static UpdateModuleScreenInfoMessage decode(PacketBuffer packetBuffer) {
        return new UpdateModuleScreenInfoMessage(
                packetBuffer.readShort(),
                packetBuffer.readRegistryId(),
                packetBuffer.readUniqueId(),
                IntStream.range(0, packetBuffer.readShort())
                        .mapToObj(number -> ModuleTab.fromPacketBuffer(packetBuffer))
                        .collect(Collectors.toList())
        );
    }
}

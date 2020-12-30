package xyz.brassgoggledcoders.transport.network.property;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.tuple.Triple;
import xyz.brassgoggledcoders.transport.Transport;

import java.util.List;
import java.util.function.Supplier;

public class UpdateContainerPropertiesMessage {
    private final short windowId;
    private final List<Triple<PropertyType<?>, Short, Object>> updates;

    public UpdateContainerPropertiesMessage(short windowId, List<Triple<PropertyType<?>, Short, Object>> updates) {
        this.windowId = windowId;
        this.updates = updates;
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeShort(windowId);

        List<Triple<PropertyType<?>, Short, Object>> validUpdates = Lists.newArrayList();
        for (Triple<PropertyType<?>, Short, Object> update : updates) {
            if (update.getLeft().isValid(update.getRight())) {
                validUpdates.add(update);
            }
        }

        packetBuffer.writeShort(validUpdates.size());
        for (Triple<PropertyType<?>, Short, Object> update : validUpdates) {
            packetBuffer.writeShort(PropertyTypes.getIndex(update.getLeft()));
            packetBuffer.writeShort(update.getMiddle());
            update.getLeft().attemptWrite(packetBuffer, update.getRight());
        }
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ClientPlayerEntity playerEntity = Minecraft.getInstance().player;
            if (playerEntity != null && playerEntity.openContainer != null) {
                Container container = playerEntity.openContainer;
                if (container.windowId == windowId) {
                    if (container instanceof IPropertyManaged) {
                        PropertyManager propertyManager = ((IPropertyManaged) container).getPropertyManager();
                        for (Triple<PropertyType<?>, Short, Object> update : updates) {
                            propertyManager.update(update.getLeft(), update.getMiddle(), update.getRight());
                        }
                    } else {
                        Transport.LOGGER.warn("Container is not instance of IPropertyManaged");
                    }
                }
            }
        });
        return true;
    }

    public static UpdateContainerPropertiesMessage decode(PacketBuffer packetBuffer) {
        short windowId = packetBuffer.readShort();
        short updateAmount = packetBuffer.readShort();
        List<Triple<PropertyType<?>, Short, Object>> updates = Lists.newArrayList();
        for (short i = 0; i < updateAmount; i++) {
            PropertyType<?> propertyType = PropertyTypes.getByIndex(packetBuffer.readShort());
            short propertyLocation = packetBuffer.readShort();
            Object object = propertyType.getReader().apply(packetBuffer);
            updates.add(Triple.of(propertyType, propertyLocation, object));
        }
        return new UpdateContainerPropertiesMessage(windowId, updates);
    }
}

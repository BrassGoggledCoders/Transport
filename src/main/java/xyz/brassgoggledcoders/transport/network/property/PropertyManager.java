package xyz.brassgoggledcoders.transport.network.property;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerListener;
import org.apache.commons.lang3.tuple.Triple;
import xyz.brassgoggledcoders.transport.Transport;

import java.util.Collection;
import java.util.List;

public class PropertyManager {
    private final List<Property<?>> properties;
    private final short windowId;

    public PropertyManager(short windowId) {
        this.windowId = windowId;
        this.properties = Lists.newArrayList();
    }

    public <T> Property<T> addTrackedProperty(Property<T> property) {
        this.properties.add(property);
        return property;
    }

    public void sendChanges(Collection<IContainerListener> containerListeners, boolean firstTime) {
        List<ServerPlayerEntity> playerListeners = Lists.newArrayList();
        for (IContainerListener listener : containerListeners) {
            if (listener instanceof ServerPlayerEntity) {
                playerListeners.add((ServerPlayerEntity) listener);
            }
        }

        if (!playerListeners.isEmpty()) {
            List<Triple<PropertyType<?>, Short, Object>> dirtyProperties = Lists.newArrayList();
            for (short i = 0; i < properties.size(); i++) {
                Property<?> property = properties.get(i);
                if (property.isDirty() || firstTime) {
                    dirtyProperties.add(Triple.of(property.getPropertyType(), i, property.get()));
                }
            }

            if (!dirtyProperties.isEmpty()) {
                for (ServerPlayerEntity playerEntity : playerListeners) {
                    Transport.instance.networkHandler.sendUpdateContainerProperties(playerEntity,
                            new UpdateContainerPropertiesMessage(windowId, dirtyProperties));
                }
            }
        }
    }

    public void update(PropertyType<?> propertyType, short propertyId, Object value) {
        Property<?> property = properties.get(propertyId);
        if (property != null && property.getPropertyType() == propertyType) {
            propertyType.attemptSet(value, property);
        }
    }
}

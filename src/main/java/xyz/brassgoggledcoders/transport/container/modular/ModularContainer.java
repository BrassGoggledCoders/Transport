package xyz.brassgoggledcoders.transport.container.modular;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportRegistries;
import xyz.brassgoggledcoders.transport.api.container.ModuleContainer;
import xyz.brassgoggledcoders.transport.api.container.ModuleContainerType;
import xyz.brassgoggledcoders.transport.util.WorldHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ModularContainer extends Container {
    private final IWorldPosCallable worldPosCallable;
    private final PlayerInventory playerInventory;
    private final List<ModuleContainer> modularContainers;

    public ModularContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory,
                            IWorldPosCallable worldPosCallable, List<ModuleContainer> modularContainers) {
        super(type, id);
        this.playerInventory = playerInventory;
        this.worldPosCallable = worldPosCallable;
        this.modularContainers = modularContainers;

    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return worldPosCallable.applyOrElse(WorldHelper.isPlayerNear(player)::test, true);
    }

    @Nonnull
    public static ModularContainer create(ContainerType<ModularContainer> containerType, int windowId,
                                          PlayerInventory playerInventory, @Nullable PacketBuffer packetBuffer) {
        List<ModuleContainer> moduleContainerList = Lists.newArrayList();
        if (packetBuffer != null) {
            int subContainers = packetBuffer.readInt();
            for (int i = 0; i < subContainers; i++) {
                String failedType = null;
                try {
                    ResourceLocation moduleContainerType = packetBuffer.readResourceLocation();
                    failedType = moduleContainerType.toString();
                    ModuleContainer moduleContainer = getType(moduleContainerType)
                            .map(type -> type.create(windowId, playerInventory, packetBuffer))
                            .orElseThrow(() -> new IllegalStateException("Failed to Find Module Container Type"));
                    if (packetBuffer.readInt() != i) {
                        throw new IllegalStateException("Module Container did read properly from PacketBuffer");
                    }
                    moduleContainerList.add(moduleContainer);
                } catch (Exception exception) {
                    if (failedType == null) {
                        Transport.LOGGER.error("Failed to create Module Container", exception);
                    } else {
                        Transport.LOGGER.error("Failed to create Module Container: " + failedType, exception);
                    }
                }
            }
        }

        return new ModularContainer(containerType, windowId, playerInventory, IWorldPosCallable.DUMMY, moduleContainerList);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T extends ModuleContainer> Optional<ModuleContainerType<T>> getType(ResourceLocation resourceLocation) {
        return Optional.ofNullable(TransportRegistries.MODULE_CONTAINER_TYPES.getValue(resourceLocation))
                .map(type -> (ModuleContainerType<T>) type);
    }
}

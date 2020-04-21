package xyz.brassgoggledcoders.transport.content;

import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.network.locator.instance.EmptyLocatorInstance;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.container.EntityLocatorInstance;

public class TransportContainers {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS =
            new DeferredRegister<>(ForgeRegistries.CONTAINERS, Transport.ID);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final RegistryObject<ContainerType<BasicAddonContainer>> MODULE = CONTAINERS.register("module",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
                Entity entity = inv.player.getEntityWorld().getEntityByID(data.readInt());
                ModuleType moduleType = TransportAPI.getModuleType(data.readResourceLocation());
                if (entity instanceof IModularEntity && moduleType != null) {
                    ModuleInstance<?> moduleInstance = ((IModularEntity) entity).getModuleInstance(moduleType);
                    if (moduleInstance != null) {
                        return new BasicAddonContainer(moduleInstance, new EntityLocatorInstance(entity),
                                IWorldPosCallable.DUMMY, inv, windowId);
                    }
                }

                Transport.LOGGER.warn("Failed to find Module for Container");
                return new BasicAddonContainer(new Object(), new EmptyLocatorInstance(), IWorldPosCallable.DUMMY, inv,
                        windowId);
            }));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}

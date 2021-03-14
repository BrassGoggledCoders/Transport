package xyz.brassgoggledcoders.transport.api;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.transport.api.navigation.NavigationPointType;

public class TransportRegistries {
    public static final IForgeRegistry<NavigationPointType> NAVIGATION_POINT_TYPES = getRegistry(NavigationPointType.class);

    public static <T extends IForgeRegistryEntry<T>> IForgeRegistry<T> getRegistry(Class<T> tClass) {
        IForgeRegistry<T> forgeRegistry = RegistryManager.ACTIVE.getRegistry(tClass);
        if (forgeRegistry != null) {
            return forgeRegistry;
        } else {
            throw new IllegalStateException("Failed to Find Registry, Likely called too early");
        }
    }
}

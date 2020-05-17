package xyz.brassgoggledcoders.transport.api.routing;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.TransportAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RoutingStorageProvider implements ICapabilityProvider {
    private final LazyOptional<RoutingStorage> routingStorageLazyOptional;

    public RoutingStorageProvider() {
        RoutingStorage routingStorage = new RoutingStorage();
        this.routingStorageLazyOptional = LazyOptional.of(() -> routingStorage);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return TransportAPI.ROUTING_STORAGE.orEmpty(cap, routingStorageLazyOptional);
    }
}

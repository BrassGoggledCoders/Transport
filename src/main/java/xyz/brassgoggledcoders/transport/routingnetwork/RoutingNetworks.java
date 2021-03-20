package xyz.brassgoggledcoders.transport.routingnetwork;

import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public enum RoutingNetworks {
    SHIP("ships");

    private final String name;

    RoutingNetworks(String name) {
        this.name = name;
    }

    @Nullable
    public RoutingNetwork getFor(IWorld world) {
        if (world instanceof ServerWorld) {
            String dataName = "transport_" + name;
            return ((ServerWorld) world).getSavedData().getOrCreate(() -> new RoutingNetwork(dataName), dataName);
        } else {
            return null;
        }
    }
}

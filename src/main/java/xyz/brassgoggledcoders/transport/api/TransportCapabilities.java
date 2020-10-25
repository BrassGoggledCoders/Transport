package xyz.brassgoggledcoders.transport.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationNetwork;

public class TransportCapabilities {
    @CapabilityInject(INavigationNetwork.class)
    public static Capability<INavigationNetwork> NAVIGATION_NETWORK;
}

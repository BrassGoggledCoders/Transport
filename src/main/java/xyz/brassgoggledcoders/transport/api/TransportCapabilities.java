package xyz.brassgoggledcoders.transport.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationNetwork;
import xyz.brassgoggledcoders.transport.api.navigation.INavigator;

public class TransportCapabilities {
    @CapabilityInject(INavigationNetwork.class)
    public static Capability<INavigationNetwork> NAVIGATION_NETWORK;

    @CapabilityInject(INavigator.class)
    public static Capability<INavigator> NAVIGATOR;
}

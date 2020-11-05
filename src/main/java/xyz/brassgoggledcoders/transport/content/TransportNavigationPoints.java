package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.navigation.NavigationPointType;
import xyz.brassgoggledcoders.transport.navigation.ConnectorNavigationPoint;
import xyz.brassgoggledcoders.transport.navigation.RandomNavigationPoint;

public class TransportNavigationPoints {
    public static final RegistryEntry<NavigationPointType> CONNECTOR = Transport.getRegistrate()
            .object("connector")
            .simple(NavigationPointType.class, () -> NavigationPointType.of(ConnectorNavigationPoint::new));

    public static final RegistryEntry<NavigationPointType> RANDOM = Transport.getRegistrate()
            .object("random")
            .simple(NavigationPointType.class, () -> NavigationPointType.of(RandomNavigationPoint::new));


    public static void setup() {

    }
}

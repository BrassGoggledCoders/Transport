package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.navigation.NavigationPointType;
import xyz.brassgoggledcoders.transport.navigation.ConnectorNavigationPoint;

public class TransportNavigationPoints {
    public static RegistryEntry<NavigationPointType> CONNECTOR = Transport.getRegistrate()
            .object("connector")
            .simple(NavigationPointType.class, () -> NavigationPointType.of(ConnectorNavigationPoint::new));


    public static void setup() {

    }
}

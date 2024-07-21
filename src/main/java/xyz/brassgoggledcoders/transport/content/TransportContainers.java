package xyz.brassgoggledcoders.transport.content;

import xyz.brassgoggledcoders.shadyskies.registering.menu.MenuRegisteringEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.menu.PatternedRailLayerMenu;
import xyz.brassgoggledcoders.transport.menu.jobsite.RailWorkerBenchMenu;
import xyz.brassgoggledcoders.transport.screen.PatternedRailLayerScreen;
import xyz.brassgoggledcoders.transport.screen.RailWorkerBenchScreen;

public class TransportContainers {

    public static MenuRegisteringEntry<RailWorkerBenchMenu> RAIL_WORKER_BENCH = Transport.getRegistering()
            .object("rail_worker_bench")
            .<RailWorkerBenchMenu, RailWorkerBenchScreen>menu(RailWorkerBenchMenu::new)
            .withScreenConstructor(() -> RailWorkerBenchScreen::new)
            .register();

    public static MenuRegisteringEntry<PatternedRailLayerMenu> PATTERNED_RAIL_LAYER = Transport.getRegistering()
            .object("patterned_rail_layer")
            .menu(PatternedRailLayerMenu::new)
            .withScreenConstructor(() -> PatternedRailLayerScreen::new)
            .register();

    public static void setup() {

    }
}

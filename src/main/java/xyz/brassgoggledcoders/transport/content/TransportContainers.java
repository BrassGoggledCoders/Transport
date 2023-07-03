package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.inventory.MenuType;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.menu.PatternedRailLayerMenu;
import xyz.brassgoggledcoders.transport.menu.jobsite.RailWorkerBenchMenu;
import xyz.brassgoggledcoders.transport.screen.PatternedRailLayerScreen;
import xyz.brassgoggledcoders.transport.screen.RailWorkerBenchScreen;

public class TransportContainers {

    public static RegistryEntry<MenuType<RailWorkerBenchMenu>> RAIL_WORKER_BENCH = Transport.getRegistrate()
            .object("rail_worker_bench")
            .menu(RailWorkerBenchMenu::new, () -> RailWorkerBenchScreen::new)
            .register();

    public static MenuEntry<PatternedRailLayerMenu> PATTERNED_RAIL_LAYER = Transport.getRegistrate()
            .object("patterned_rail_layer")
            .menu(
                    (type, windowId, inv) -> new PatternedRailLayerMenu(type, windowId, inv),
                    () -> PatternedRailLayerScreen::new
            )
            .register();

    public static void setup() {

    }
}

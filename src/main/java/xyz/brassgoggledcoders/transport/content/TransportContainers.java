package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.inventory.MenuType;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.menu.RailWorkerBenchMenu;
import xyz.brassgoggledcoders.transport.screen.RailWorkerBenchScreen;

public class TransportContainers {

    public static RegistryEntry<MenuType<RailWorkerBenchMenu>> RAIL_WORKER_BENCH = Transport.getRegistrate()
            .object("rail_worker_bench")
            .menu(RailWorkerBenchMenu::new, () -> RailWorkerBenchScreen::new)
            .register();

    public static void setup() {

    }
}

package xyz.brassgoggledcoders.transport;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.transport.compat.top.TransportTOP;
import xyz.brassgoggledcoders.transport.content.*;
import xyz.brassgoggledcoders.transport.network.NetworkHandler;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static xyz.brassgoggledcoders.transport.Transport.ID;

@Mod(ID)
public class Transport {
    public static final String ID = "transport";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final Registering REGISTERING = Registering.of(ID);

    public static final NetworkHandler NETWORK = new NetworkHandler();

    public Transport(IEventBus modEventBus) {
        REGISTERING.setModBus(modEventBus);

        TransportBlocks.setup();
        TransportContainers.setup();
        TransportEntities.setup();
        TransportItems.setup();
        TransportRecipes.setup(modEventBus);
        TransportShellContent.setup(modEventBus);

        loadCompat(modEventBus, "theoneprobe", () -> TransportTOP::new);
    }

    public void loadCompat(IEventBus eventBus, String modid, Supplier<Consumer<IEventBus>> compatRunner) {
        if (ModList.get().isLoaded(modid)) {
            compatRunner.get()
                    .accept(eventBus);
        }
    }

    public static Registering getRegistering() {
        return REGISTERING;
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

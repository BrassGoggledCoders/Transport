package xyz.brassgoggledcoders.transport.compat.quark;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.transport.api.TransportAPI;

public class TransportQuark {
    public static void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        QuarkCargoModules.register(modEventBus);

        if (ModList.get().isLoaded("quark")) {
            TransportAPI.setConnectionChecker(new QuarkConnectionChecker());
        }
    }
}

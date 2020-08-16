package xyz.brassgoggledcoders.transport.quark;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.quark.connection.QuarkConnectionChecker;
import xyz.brassgoggledcoders.transport.quark.content.QuarkCargoModules;

@Mod(TransportQuark.ID)
public class TransportQuark {
    public static final String ID = "transport_quark";

    public TransportQuark() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        if (ModList.get().isLoaded("quark")) {
            QuarkCargoModules.register(modEventBus);
            TransportAPI.setConnectionChecker(new QuarkConnectionChecker());
        }
    }
}

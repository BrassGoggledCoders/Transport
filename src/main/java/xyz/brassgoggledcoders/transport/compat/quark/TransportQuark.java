package xyz.brassgoggledcoders.transport.compat.quark;

import net.minecraftforge.fml.ModList;
import xyz.brassgoggledcoders.transport.api.TransportAPI;

public class TransportQuark {
    public static void setup() {
        if (ModList.get().isLoaded("quark")) {
            TransportAPI.setConnectionChecker(new QuarkConnectionChecker());
        }
    }
}

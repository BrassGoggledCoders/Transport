package xyz.brassgoggledcoders.transport.provider;

import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class TransportDataGenerator {

    public static void gather(GatherDataEvent event) {
        new TransportRecipeProvider(event.getGenerator());
    }
}

package xyz.brassgoggledcoders.transport.provider;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class TransportDataGenerator {

    public static void gather(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();

        if (event.includeServer()) {
            dataGenerator.addProvider(new TransportRecipeProvider(dataGenerator));
        }
    }
}

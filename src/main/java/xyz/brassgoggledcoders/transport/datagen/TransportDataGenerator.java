package xyz.brassgoggledcoders.transport.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import xyz.brassgoggledcoders.transport.datagen.loot.TransportLootTableProvider;

public class TransportDataGenerator {

    public static void gather(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();

        if (event.includeServer()) {
            dataGenerator.addProvider(new TransportRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new TransportLootTableProvider(dataGenerator));
        }
    }
}

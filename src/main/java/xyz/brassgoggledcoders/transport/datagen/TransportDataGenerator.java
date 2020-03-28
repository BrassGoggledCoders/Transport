package xyz.brassgoggledcoders.transport.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import xyz.brassgoggledcoders.transport.datagen.loot.TransportLootTableProvider;
import xyz.brassgoggledcoders.transport.datagen.tags.TransportBlockTagsProvider;
import xyz.brassgoggledcoders.transport.datagen.tags.TransportItemTagsProvider;

public class TransportDataGenerator {

    public static void gather(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();

        if (event.includeServer()) {
            dataGenerator.addProvider(new TransportRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new TransportLootTableProvider(dataGenerator));
            dataGenerator.addProvider(new TransportBlockTagsProvider(dataGenerator));
            dataGenerator.addProvider(new TransportItemTagsProvider(dataGenerator));
        }

        if (event.includeClient()) {
            dataGenerator.addProvider(new TransportLanguageProvider(dataGenerator));
        }
    }
}

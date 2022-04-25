package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PatternedRailLayerCustomLoaderBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    public PatternedRailLayerCustomLoaderBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(PatternedRailLayerModelLoader.ID, parent, existingFileHelper);
    }
}

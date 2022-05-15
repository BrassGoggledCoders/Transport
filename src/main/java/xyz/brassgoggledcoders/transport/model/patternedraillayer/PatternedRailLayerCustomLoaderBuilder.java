package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Map;

public class PatternedRailLayerCustomLoaderBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    private final Map<String, ResourceLocation> layers;

    public PatternedRailLayerCustomLoaderBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(PatternedRailLayerModelLoader.ID, parent, existingFileHelper);
        this.layers = Maps.newHashMap();
    }

    @SuppressWarnings("UnusedReturnValue")
    public PatternedRailLayerCustomLoaderBuilder<T> withLayer(ResourceLocation location) {
        this.layers.put("layer" + layers.size(), location);
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        JsonObject jsonObject = super.toJson(json);

        this.layers.forEach((key, value) -> jsonObject.addProperty(key, value.toString()));

        return jsonObject;
    }
}

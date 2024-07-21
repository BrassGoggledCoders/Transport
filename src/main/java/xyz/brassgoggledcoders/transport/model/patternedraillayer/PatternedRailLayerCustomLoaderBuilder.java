package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.checkerframework.checker.lock.qual.NewObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PatternedRailLayerCustomLoaderBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    private final Map<String, ResourceLocation> layers;

    public PatternedRailLayerCustomLoaderBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(PatternedRailLayerModelLoader.ID, parent, existingFileHelper, false);
        this.layers = Maps.newHashMap();
    }

    @SuppressWarnings("UnusedReturnValue")
    public PatternedRailLayerCustomLoaderBuilder<T> withLayer(ResourceLocation location) {
        this.layers.put("layer" + layers.size(), location);
        return this;
    }

    @Override
    @NotNull
    public JsonObject toJson(@NotNull JsonObject json) {
        JsonObject jsonObject = super.toJson(json);

        this.layers.forEach((key, value) -> jsonObject.addProperty(key, value.toString()));

        return jsonObject;
    }
}

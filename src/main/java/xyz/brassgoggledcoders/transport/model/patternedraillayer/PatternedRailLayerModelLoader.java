package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.Transport;

public class PatternedRailLayerModelLoader implements IModelLoader<PatternedRailLayerModelGeometry> {
    public static final ResourceLocation ID = Transport.rl("patterned_rail_layer");

    @Override
    @NotNull
    public PatternedRailLayerModelGeometry read(@NotNull JsonDeserializationContext deserializationContext, @NotNull JsonObject modelContents) {
        return new PatternedRailLayerModelGeometry();
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager pResourceManager) {

    }
}

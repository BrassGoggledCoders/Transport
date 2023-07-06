package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.Transport;

import java.util.Optional;

public class PatternedRailLayerModelLoader implements IGeometryLoader<PatternedRailLayerModelGeometry> {
    public static final ResourceLocation ID = Transport.rl("patterned_rail_layer");

    @Override
    @NotNull
    public PatternedRailLayerModelGeometry read(@NotNull JsonObject modelContents, @NotNull JsonDeserializationContext deserializationContext) {
        ImmutableList.Builder<Material> materialBuilder = ImmutableList.builder();
        for (int i = 0; modelContents.has("layer" + i); i++) {
            String layer = "layer" + i;
            ResourceLocation resourceLocation = ResourceLocation.tryParse(GsonHelper.getAsString(modelContents, layer));
            materialBuilder.add(
                    Optional.ofNullable(resourceLocation)
                            .map(name -> new Material(InventoryMenu.BLOCK_ATLAS, name))
                            .orElseThrow(() -> new JsonParseException(layer + " is an invalid resource location"))
            );
        }
        ImmutableList<Material> materials = materialBuilder.build();
        if (materials.isEmpty()) {
            throw new JsonParseException("Did not found any valid resource locations for field 'background' ");
        }
        return new PatternedRailLayerModelGeometry(materials);
    }
}

package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class PatternedRailLayerModelGeometry implements IModelGeometry<PatternedRailLayerModelGeometry> {
    private final ImmutableMap<String, Material> textures;
    private final ItemLayerModel model;

    public PatternedRailLayerModelGeometry(ImmutableMap<String, Material> textures) {
        this.textures = textures;
        this.model = new ItemLayerModel(textures.values().asList());
    }

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter,
                           ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new PatternedRailLayerBakedModel(model.bake(
                owner,
                bakery,
                spriteGetter,
                modelTransform,
                overrides,
                modelLocation
        ));
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter,
                                            Set<Pair<String, String>> missingTextureErrors) {
        return model.getTextures(
                new OverrideModelConfiguration(
                        owner,
                        textures
                ),
                modelGetter,
                missingTextureErrors
        );
    }
}

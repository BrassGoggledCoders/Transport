package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public record PatternedRailLayerModelGeometry(
        ImmutableList<Material> textures
) implements IModelGeometry<PatternedRailLayerModelGeometry> {

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter,
                           ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new PatternedRailLayerParentBakedModel(this.textures);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter,
                                            Set<Pair<String, String>> missingTextureErrors) {
        return textures();
    }
}

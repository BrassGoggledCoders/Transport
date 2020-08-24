package xyz.brassgoggledcoders.transport.model.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ItemMultiLayerBakedModel;
import net.minecraftforge.client.model.ModelTransformComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class ModularItemModel implements IModelGeometry<ModularItemModel> {
    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery,
                            Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
                            ItemOverrideList overrides, ResourceLocation modelLocation) {
        return ItemMultiLayerBakedModel.builder(owner, spriteGetter.apply(new RenderMaterial(
                        PlayerContainer.LOCATION_BLOCKS_TEXTURE, MissingTextureSprite.getLocation())),
                new ModularItemItemOverrideList(), PerspectiveMapWrapper.getTransforms(new ModelTransformComposition(
                        owner.getCombinedTransform(), modelTransform)))
                .build();
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner,
                                                  Function<ResourceLocation, IUnbakedModel> modelGetter,
                                                  Set<Pair<String, String>> missingTextureErrors) {
        return Collections.emptyList();
    }
}

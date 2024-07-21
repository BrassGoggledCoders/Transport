package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public record PatternedRailLayerModelGeometry(
        ImmutableList<Material> textures
) implements IUnbakedGeometry<PatternedRailLayerModelGeometry> {

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public BakedModel bake(IGeometryBakingContext owner, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter,
                           ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new PatternedRailLayerParentBakedModel(
                owner.getTransforms(),
                this.textures
        );
    }
}

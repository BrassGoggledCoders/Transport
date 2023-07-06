package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record PatternedRailLayerChildBakedModel(
        BakedModel railModel,
        ItemTransforms defaultTransforms,
        ImmutableList<Material> background
) implements BakedModel {

    @Override
    @NotNull
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, @NotNull RandomSource pRand) {
        List<BakedQuad> bakedQuads;
        if (railModel() != null) {
            bakedQuads = new ArrayList<>(railModel().getQuads(pState, pSide, pRand));
        } else {
            bakedQuads = new ArrayList<>();
        }
        float offset = 0F;
        for (Material material : background()) {
            var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(-1, material.sprite());
            SimpleModelState modelState = new SimpleModelState(
                    new Transformation(Matrix4f.createTranslateMatrix(0, 0, offset -= 0.0825))
            );
            bakedQuads.addAll(UnbakedGeometryHelper.bakeElements(unbaked, $ -> material.sprite(), modelState, null));

        }
        return bakedQuads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return railModel() != null && railModel().useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return railModel() != null && railModel().isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return railModel() != null && railModel().useAmbientOcclusion();
    }

    @Override
    public boolean isCustomRenderer() {
        return railModel() != null && railModel().isCustomRenderer();
    }

    @Override
    @NotNull
    public TextureAtlasSprite getParticleIcon() {
        return background().get(0).sprite();
    }

    @Override
    @NotNull
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public ItemTransforms getTransforms() {
        return this.railModel() == null ? this.defaultTransforms() : this.railModel().getTransforms();
    }
}

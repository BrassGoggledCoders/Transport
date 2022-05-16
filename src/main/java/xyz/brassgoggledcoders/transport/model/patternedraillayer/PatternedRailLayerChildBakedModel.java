package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record PatternedRailLayerChildBakedModel(
        BakedModel railModel,
        ImmutableList<Material> background,
        ImmutableMap<ItemTransforms.TransformType, Transformation> defaultTransforms
) implements BakedModel {

    @Override
    @NotNull
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, @NotNull Random pRand) {
        List<BakedQuad> bakedQuads;
        if (railModel() != null) {
            bakedQuads = new ArrayList<>(railModel().getQuads(pState, pSide, pRand, EmptyModelData.INSTANCE));
        } else {
            bakedQuads = new ArrayList<>();
        }
        float offset = 0F;
        for (Material material : background()) {
            bakedQuads.addAll(
                    ItemLayerModel.getQuadsForSprite(-1,
                            material.sprite(),
                            new Transformation(Matrix4f.createTranslateMatrix(0, 0, offset -= 0.0825))
                    )
            );
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
        return railModel() == null ? ItemTransforms.NO_TRANSFORMS : railModel().getTransforms();
    }

    @Override
    public boolean doesHandlePerspectives() {
        return railModel() == null || railModel().doesHandlePerspectives();
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack poseStack) {
        return PerspectiveMapWrapper.handlePerspective(this, defaultTransforms, cameraTransformType, poseStack);
    }
}

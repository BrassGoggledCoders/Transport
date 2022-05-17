package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PatternedRailLayerParentBakedModel implements BakedModel {
    private final PatternedRailLayerItemOverrides itemOverrides;
    private final Material particle;

    public PatternedRailLayerParentBakedModel(ImmutableList<Material> background,
                                              ImmutableMap<ItemTransforms.TransformType, Transformation> defaultTransforms) {
        this.itemOverrides = new PatternedRailLayerItemOverrides(background, defaultTransforms);
        this.particle = background.get(0);
    }

    @Override
    @NotNull
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, @NotNull Random pRand) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    @NotNull
    public TextureAtlasSprite getParticleIcon() {
        return particle.sprite();
    }

    @Override
    @NotNull
    public ItemOverrides getOverrides() {
        return itemOverrides;
    }
}

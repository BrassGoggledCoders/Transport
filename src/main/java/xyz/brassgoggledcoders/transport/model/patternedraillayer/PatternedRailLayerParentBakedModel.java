package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.signature.qual.SignatureUnknown;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PatternedRailLayerParentBakedModel implements BakedModel {
    private final PatternedRailLayerItemOverrides itemOverrides;
    private final Material particle;

    public PatternedRailLayerParentBakedModel(ItemTransforms transforms, ImmutableList<Material> background) {
        this.itemOverrides = new PatternedRailLayerItemOverrides(transforms, background);
        this.particle = background.get(0);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecated")
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, @NotNull RandomSource pRand) {
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
    @SuppressWarnings("deprecated")
    public TextureAtlasSprite getParticleIcon() {
        return particle.sprite();
    }

    @Override
    @NotNull
    public ItemOverrides getOverrides() {
        return itemOverrides;
    }
}

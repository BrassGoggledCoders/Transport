package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.ItemMultiLayerBakedModel;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PatternedRailLayerItemOverrides extends ItemOverrides {

    private final BakedModel background;

    public PatternedRailLayerItemOverrides(BakedModel background) {
        this.background = background;
    }

    @Nullable
    @Override
    public BakedModel resolve(@NotNull BakedModel pModel, @NotNull ItemStack pStack, @Nullable ClientLevel pLevel,
                              @Nullable LivingEntity pEntity, int pSeed) {

        return new ItemMultiLayerBakedModel(
                true,
                true,
                false,
                background.getParticleIcon(EmptyModelData.INSTANCE),
                ItemOverrides.EMPTY,
                ImmutableMap.<ItemTransforms.TransformType, Transformation>builder()
                        .build(),
                ImmutableList.<Pair<BakedModel, RenderType>>builder()
                        .add(Pair.of(
                                background,
                                RenderType.solid()
                        ))
                        .add(Pair.of(
                                Minecraft.getInstance()
                                        .getItemRenderer()
                                        .getModel(new ItemStack(Items.RAIL), pLevel, pEntity, pSeed),
                                ItemBlockRenderTypes.getRenderType(new ItemStack(Items.RAIL), true)
                        ))
                        .build()
        );

    }
}

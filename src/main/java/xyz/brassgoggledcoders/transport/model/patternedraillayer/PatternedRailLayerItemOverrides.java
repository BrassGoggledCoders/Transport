package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;

import java.util.Optional;

public class PatternedRailLayerItemOverrides extends ItemOverrides {

    private final ImmutableList<Material> background;
    private final ImmutableMap<ItemTransforms.TransformType, Transformation> defaultTransforms;

    public PatternedRailLayerItemOverrides(ImmutableList<Material> background,
                                           ImmutableMap<ItemTransforms.TransformType, Transformation> defaultTransforms) {
        this.background = background;
        this.defaultTransforms = defaultTransforms;
    }

    @Nullable
    @Override
    public BakedModel resolve(@NotNull BakedModel pModel, @NotNull ItemStack pStack, @Nullable ClientLevel pLevel,
                              @Nullable LivingEntity pEntity, int pSeed) {

        ItemStack renderStack = Optional.ofNullable(pEntity)
                .flatMap(livingEntity -> livingEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                        .resolve()
                ).flatMap(inventory -> pStack.getCapability(IRailProvider.CAPABILITY)
                        .map(capability -> capability.findNext(inventory, true))
                ).orElse(ItemStack.EMPTY);
        return new PatternedRailLayerChildBakedModel(
                renderStack.isEmpty() ? null : Minecraft.getInstance()
                        .getItemRenderer()
                        .getModel(renderStack, pLevel, pEntity, pSeed),
                this.background,
                this.defaultTransforms
        );

    }
}

package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;

import java.util.Optional;

public class PatternedRailLayerItemOverrides extends ItemOverrides {

    private final ItemTransforms transforms;
    private final ImmutableList<Material> background;

    public PatternedRailLayerItemOverrides(ItemTransforms transforms, ImmutableList<Material> background) {
        this.transforms = transforms;
        this.background = background;
    }

    @Nullable
    @Override
    public BakedModel resolve(@NotNull BakedModel pModel, @NotNull ItemStack pStack, @Nullable ClientLevel pLevel,
                              @Nullable LivingEntity pEntity, int pSeed) {

        ItemStack renderStack = Optional.ofNullable(pEntity)
                .flatMap(livingEntity -> livingEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .resolve()
                ).flatMap(inventory -> pStack.getCapability(IRailProvider.CAPABILITY)
                        .map(capability -> capability.findNext(inventory, true))
                ).orElse(ItemStack.EMPTY);
        return new PatternedRailLayerChildBakedModel(
                renderStack.isEmpty() ? null : Minecraft.getInstance()
                        .getItemRenderer()
                        .getModel(renderStack, pLevel, pEntity, pSeed),
                this.transforms,
                this.background
        );

    }
}

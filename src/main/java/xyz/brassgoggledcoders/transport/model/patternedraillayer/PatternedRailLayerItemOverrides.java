package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PatternedRailLayerItemOverrides extends ItemOverrides {

    @Nullable
    @Override
    public BakedModel resolve(@NotNull BakedModel pModel, @NotNull ItemStack pStack, @Nullable ClientLevel pLevel,
                              @Nullable LivingEntity pEntity, int pSeed) {

        return Minecraft.getInstance()
                .getItemRenderer()
                .getModel(new ItemStack(Items.RAIL), pLevel, pEntity, pSeed);
    }
}

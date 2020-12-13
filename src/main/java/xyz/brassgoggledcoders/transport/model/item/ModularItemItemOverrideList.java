package xyz.brassgoggledcoders.transport.model.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.item.IHulledItem;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ModularItemItemOverrideList extends ItemOverrideList {

    public ModularItemItemOverrideList() {
    }

    @Override
    @ParametersAreNonnullByDefault
    public IBakedModel getOverrideModel(IBakedModel bakedModel, ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof IHulledItem) {
            return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(
                    ((IHulledItem) itemStack.getItem()).getHullType(itemStack).asItem());
        } else {
            return Minecraft.getInstance().getModelManager().getMissingModel();
        }
    }

}

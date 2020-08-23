package xyz.brassgoggledcoders.transport.model.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.HullType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ModularItemItemOverrideList extends ItemOverrideList {

    public ModularItemItemOverrideList() {
    }

    @Override
    @ParametersAreNonnullByDefault
    public IBakedModel func_239290_a_(IBakedModel bakedModel, ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity) {
        CompoundNBT nbt = itemStack.getTag();
        if (nbt != null && nbt.contains("hull_type", Constants.NBT.TAG_STRING)) {
            HullType hullType = TransportAPI.HULL_TYPE.get().getValue(new ResourceLocation(nbt.getString("hull_type")));
            if (hullType != null) {
                return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(hullType.asItem());
            }
        }
        return Minecraft.getInstance().getModelManager().getMissingModel();
    }

}

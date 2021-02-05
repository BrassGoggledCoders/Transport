package xyz.brassgoggledcoders.transport.item.locomotive;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportText;
import xyz.brassgoggledcoders.transport.entity.locomotive.SteamLocomotiveEntity;
import xyz.brassgoggledcoders.transport.util.ItemStackHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class SteamLocomotiveItem extends LocomotiveItem<SteamLocomotiveEntity> {
    public SteamLocomotiveItem(Properties builder) {
        super(TransportEntities.STEAM_LOCOMOTIVE, builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, world, tooltip, flagIn);
        CompoundNBT locomotiveData = stack.getChildTag("locomotiveData");
        if (locomotiveData != null && locomotiveData.contains("engine")) {
            CompoundNBT engineNBT = locomotiveData.getCompound("engine");
            FluidStack water = FluidStack.loadFluidStackFromNBT(engineNBT.getCompound("waterTank"));
            CompoundNBT fuelHandler = engineNBT.getCompound("fuelHandler");
            boolean addedContents = false;
            if (fuelHandler.contains("Items")) {
                NonNullList<ItemStack> itemStacks = ItemStackHelper.loadItemStacks(fuelHandler);
                if (itemStacks.size() > 0) {
                    addedContents = true;
                    tooltip.add(TransportText.TOOLTIP_CONTENTS);

                    for (ItemStack itemStack : itemStacks) {
                        tooltip.add(new TranslationTextComponent("tooltip.transport.contents.item",
                                itemStack.getCount(), itemStack.getDisplayName()));
                    }
                }
            }
            if (!water.isEmpty()) {
                if (!addedContents) {
                    tooltip.add(TransportText.TOOLTIP_CONTENTS);
                }
                tooltip.add(new TranslationTextComponent("tooltip.transport.contents.fluid",
                        water.getAmount(), water.getFluid().getAttributes().getDisplayName(water)));
            }

        }
    }
}

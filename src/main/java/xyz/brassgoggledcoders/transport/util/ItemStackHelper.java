package xyz.brassgoggledcoders.transport.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

public class ItemStackHelper {
    public static NonNullList<ItemStack> loadItemStacks(CompoundNBT compoundNBT) {
        if (compoundNBT.contains("Items")) {
            ListNBT listNBT = compoundNBT.getList("Items", 10);
            NonNullList<ItemStack> itemStacks = NonNullList.create();
            for (int i = 0; i < listNBT.size(); ++i) {
                CompoundNBT slotNBT = listNBT.getCompound(i);
                int j = slotNBT.getByte("Slot") & 255;
                itemStacks.add(j, ItemStack.read(slotNBT));
            }
            return itemStacks;
        } else {
            return NonNullList.create();
        }
    }
}

package xyz.brassgoggledcoders.transport.api.transfer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemTransferor implements ITransferor<IItemHandler> {
    @Override
    public Capability<IItemHandler> getCapability() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public boolean transfer(IItemHandler from, IItemHandler to, int transferAmount) {
        int moved = 0;
        int slotNumber = 0;
        do {
            ItemStack itemStack = from.extractItem(slotNumber, 16, true);
            if (!itemStack.isEmpty()) {
                if (ItemHandlerHelper.insertItem(to, itemStack, true).isEmpty()) {
                    ItemStack movedItemStack = from.extractItem(slotNumber, 16, false);
                    moved += movedItemStack.getCount();
                    ItemHandlerHelper.insertItem(to, movedItemStack, false);
                }
            }
            slotNumber++;
        } while (slotNumber < from.getSlots() && moved < transferAmount);
        return moved > 0;
    }

    @Override
    public int getDefaultAmount() {
        return 64;
    }
}

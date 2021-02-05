package xyz.brassgoggledcoders.transport.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.transport.api.item.IModularItem;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ModularItemStackHandler;

import javax.annotation.Nonnull;

public class ModularItemSlot extends SlotItemHandler {

    public ModularItemSlot(ModularItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IModularItem<?>;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return 1;
    }
}

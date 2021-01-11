package xyz.brassgoggledcoders.transport.capability.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class FuelItemStackHandler extends ItemStackHandler {
    public FuelItemStackHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return ForgeHooks.getBurnTime(stack) > 0;
    }

    public int burnFuel() {
        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack itemStack = this.getStackInSlot(i);
            int burnTime = ForgeHooks.getBurnTime(itemStack);
            if (burnTime > 0) {
                if (itemStack.hasContainerItem()) {
                    this.setStackInSlot(i, itemStack.getContainerItem());
                } else {
                    itemStack.shrink(1);
                }
                return burnTime;
            }
        }
        return 0;
    }
}

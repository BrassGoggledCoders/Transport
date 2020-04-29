package xyz.brassgoggledcoders.transport.capability.itemhandler.furnaceminecart;

import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class FurnaceMinecartFuelHandler implements IItemHandler {
    private final FurnaceMinecartEntity furnaceMinecartEntity;

    public FurnaceMinecartFuelHandler(FurnaceMinecartEntity furnaceMinecartEntity) {
        this.furnaceMinecartEntity = furnaceMinecartEntity;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.hasContainerItem()) {
            int burnTime = ForgeHooks.getBurnTime(stack);
            if (furnaceMinecartEntity.fuel + burnTime < 32000) {
                ItemStack newStack = stack.getContainerItem();
                if (!simulate) {
                    furnaceMinecartEntity.fuel += burnTime;
                }
                return newStack;
            }
        } else {
            int burnTime = ForgeHooks.getBurnTime(stack);
            if (furnaceMinecartEntity.fuel + burnTime < 32000) {
                ItemStack newStack = stack.copy();
                int burned = 1;
                newStack.shrink(1);
                while (!newStack.isEmpty() && burnTime * ++burned + furnaceMinecartEntity.fuel < 32000) {
                    newStack.shrink(1);
                }

                if (!simulate) {
                    furnaceMinecartEntity.fuel += --burned * burnTime;
                }
                return newStack;
            }
        }

        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return ForgeHooks.getBurnTime(stack) > 0;
    }
}

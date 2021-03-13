package xyz.brassgoggledcoders.transport.api.helper;

import com.mojang.datafixers.util.Function4;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.transport.Transport;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ContainerHelper {
    public static void addPlayerSlots(PlayerInventory playerInventory, Consumer<Slot> addSlot) {
        addPlayerSlots(playerInventory, addSlot, 8, 84);
    }

    public static void addPlayerSlots(PlayerInventory playerInventory, Consumer<Slot> addSlot, int startX, int startY) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot.accept(new Slot(playerInventory, j + i * 9 + 9, startX + j * 18, startY + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            addSlot.accept(new Slot(playerInventory, k, startX + k * 18, startY + 58));
        }
    }

    @Nonnull
    public static ItemStack transferStackInSlot(@Nonnull Container container, @Nonnull PlayerEntity player, int index,
                                         Function4<ItemStack, Integer, Integer, Boolean, Boolean> merge) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = container.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotItemStack = slot.getStack();
            itemstack = slotItemStack.copy();
            int containerSlots = container.inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!merge.apply(slotItemStack, containerSlots, container.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!merge.apply(slotItemStack, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItemStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotItemStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotItemStack);
        }

        return itemstack;
    }
}

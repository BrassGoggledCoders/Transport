package xyz.brassgoggledcoders.transport.shellcontent.storage.item;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public record CapabilityContainer(
        IItemHandlerModifiable itemHandler,
        Predicate<Player> stillValid
) implements Container {

    @Override
    public int getContainerSize() {
        return itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getItem(int pIndex) {
        return itemHandler.getStackInSlot(pIndex);
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = itemHandler.getStackInSlot(slot);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
    }

    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack s = getItem(index);
        if (s.isEmpty()) {
            return ItemStack.EMPTY;
        }
        setItem(index, ItemStack.EMPTY);
        return s;
    }

    @Override
    public void setItem(int pIndex, @Nonnull ItemStack pStack) {
        itemHandler.setStackInSlot(pIndex, pStack);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(@Nonnull Player pPlayer) {
        return stillValid.test(pPlayer);
    }

    @Override
    public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
        return itemHandler.isItemValid(slot, stack);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}

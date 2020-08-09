package xyz.brassgoggledcoders.transport.capability.itemhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class InventoryWrapper implements IInventory {
    private final IItemHandlerModifiable iItemHandler;
    private final Predicate<PlayerEntity> canInteract;

    public InventoryWrapper(IItemHandlerModifiable iItemHandler, Predicate<PlayerEntity> canInteract) {
        this.iItemHandler = iItemHandler;
        this.canInteract = canInteract;
    }

    @Override
    public int getSizeInventory() {
        return iItemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return IntStream.range(0, this.getSizeInventory())
                .mapToObj(this::getStackInSlot)
                .allMatch(ItemStack::isEmpty);
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int index) {
        return iItemHandler.getStackInSlot(index);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = iItemHandler.getStackInSlot(index);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        ItemStack s = getStackInSlot(index);
        if(s.isEmpty()) return ItemStack.EMPTY;
        setInventorySlotContents(index, ItemStack.EMPTY);
        return s;
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        iItemHandler.setStackInSlot(index, stack);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return canInteract.test(player);
    }

    @Override
    public void clear() {

    }
}

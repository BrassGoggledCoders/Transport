package xyz.brassgoggledcoders.transport.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ModularItemStackHandler;
import xyz.brassgoggledcoders.transport.content.TransportModuleSlots;

import javax.annotation.Nonnull;
import java.util.List;

public class ModuleSlotSlot extends Slot {
    private static final IInventory EMPTY_INVENTORY = new Inventory(0);

    private final ModularItemStackHandler itemStackHandler;

    public ModuleSlotSlot(ModularItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
        super(EMPTY_INVENTORY, index, xPosition, yPosition);
        this.itemStackHandler = itemHandler;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && itemStackHandler.isItemValid(this.getSlotIndex() + 1, stack);
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isSameInventory(@Nonnull Slot other) {
        return other instanceof ModuleSlotSlot && ((ModuleSlotSlot) other).itemStackHandler == this.itemStackHandler;
    }

    @Override
    public boolean isEnabled() {
        return this.itemStackHandler.getAvailableModuleSlots().size() > this.getSlotIndex() ;
    }

    @Override
    @Nonnull
    public ItemStack getStack() {
        return this.itemStackHandler.getStackInSlot(this.getSlotIndex() + 1);
    }

    // Override if your IItemHandler does not implement IItemHandlerModifiable
    @Override
    public void putStack(@Nonnull ItemStack stack) {
        this.itemStackHandler.setStackInSlot(this.getSlotIndex() + 1, stack);
        this.onSlotChanged();
    }

    @Override
    public void onSlotChange(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {

    }

    @Override
    public int getSlotStackLimit() {
        return this.itemStackHandler.getSlotLimit(this.getSlotIndex() + 1);
    }

    @Override
    public boolean canTakeStack(@Nonnull PlayerEntity player) {
        return !this.itemStackHandler.extractItem(this.getSlotIndex() + 1, 1, true).isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int amount) {
        return itemStackHandler.extractItem(this.getSlotIndex() + 1, amount, false);
    }

    public ModuleSlot getModuleSlot() {
        List<ModuleSlot> availableModuleSlots = itemStackHandler.getAvailableModuleSlots();
        if (availableModuleSlots.size() > this.getSlotIndex()) {
            return availableModuleSlots.get(this.getSlotIndex());
        } else {
            return TransportModuleSlots.NONE.get();
        }
    }
}

package xyz.brassgoggledcoders.transport.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.transport.api.item.IModularItem;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ModularItemStackHandler;

import javax.annotation.Nonnull;

public class ModularItemSlot extends SlotItemHandler {
    private final boolean readOnly;

    public ModularItemSlot(ModularItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
        this(itemHandler, index, xPosition, yPosition, false);
    }

    public ModularItemSlot(ModularItemStackHandler itemHandler, int index, int xPosition, int yPosition, boolean readOnly) {
        super(itemHandler, index, xPosition, yPosition);
        this.readOnly = readOnly;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return !readOnly && !stack.isEmpty() && stack.getItem() instanceof IModularItem<?>;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return !readOnly && super.canTakeStack(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount) {
        return readOnly ? ItemStack.EMPTY : super.decrStackSize(amount);
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
        super.putStack(stack);
    }
}

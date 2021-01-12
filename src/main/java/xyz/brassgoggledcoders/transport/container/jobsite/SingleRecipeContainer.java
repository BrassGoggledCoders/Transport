package xyz.brassgoggledcoders.transport.container.jobsite;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.recipe.jobsite.SingleItemSizedRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class SingleRecipeContainer<T extends SingleItemSizedRecipe> extends Container {
    private final IWorldPosCallable worldPosCallable;
    private final IntReferenceHolder selectedRecipe = IntReferenceHolder.single();
    private final World world;
    private List<T> recipes = Lists.newArrayList();
    private ItemStack itemStackInput = ItemStack.EMPTY;
    final Slot inputInventorySlot;
    final Slot outputInventorySlot;
    private Runnable inventoryUpdateListener = () -> {
    };
    public final IInventory inputInventory = new Inventory(1) {
        public void markDirty() {
            super.markDirty();
            onCraftMatrixChanged(this);
            inventoryUpdateListener.run();
        }
    };
    private final CraftResultInventory inventory = new CraftResultInventory();
    private final IRecipeType<T> recipeType;

    public SingleRecipeContainer(ContainerType<? extends SingleRecipeContainer<?>> containerType, int windowId,
                                 PlayerInventory playerInventory, final IWorldPosCallable worldPosCallable,
                                 IRecipeType<T> recipeType) {
        super(containerType, windowId);
        this.worldPosCallable = worldPosCallable;
        this.world = playerInventory.player.world;
        this.recipeType = recipeType;
        this.inputInventorySlot = this.addSlot(new Slot(this.inputInventory, 0, 20, 33));
        this.outputInventorySlot = this.addSlot(new SingleRecipeOutputSlot(this.inventory, 1, 143,
                33, this.worldPosCallable, this::attemptCraft));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.trackInt(this.selectedRecipe);
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<T> getRecipeList() {
        return this.recipes;
    }

    public int getRecipeListSize() {
        return this.recipes.size();
    }

    public boolean hasItemsInInputSlot() {
        return this.inputInventorySlot.getHasStack() && !this.recipes.isEmpty();
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return isWithinUsableDistance(this.worldPosCallable, player, this.getBlock());
    }

    protected abstract Block getBlock();

    @Override
    public boolean enchantItem(@Nonnull PlayerEntity player, int id) {
        if (this.validateId(id)) {
            this.selectedRecipe.set(id);
            this.updateRecipeResultSlot();
        }

        return true;
    }

    private boolean validateId(int id) {
        return id >= 0 && id < this.recipes.size();
    }

    @Override
    public void onCraftMatrixChanged(@Nonnull IInventory inventory) {
        ItemStack itemstack = this.inputInventorySlot.getStack();
        if (itemstack.getItem() != this.itemStackInput.getItem()) {
            this.itemStackInput = itemstack.copy();
            this.updateAvailableRecipes(inventory, itemstack);
        }
    }

    private void updateAvailableRecipes(IInventory inventoryIn, ItemStack stack) {
        this.recipes.clear();
        this.selectedRecipe.set(-1);
        this.outputInventorySlot.putStack(ItemStack.EMPTY);
        if (!stack.isEmpty()) {
            this.recipes = this.world.getRecipeManager().getRecipes(this.recipeType, inventoryIn, this.world);
        }
    }

    private void updateRecipeResultSlot() {
        if (!this.recipes.isEmpty() && this.validateId(this.selectedRecipe.get())) {
            T recipe = this.recipes.get(this.selectedRecipe.get());
            this.outputInventorySlot.putStack(recipe.getCraftingResult(this.inputInventory));
        } else {
            this.outputInventorySlot.putStack(ItemStack.EMPTY);
        }

        this.detectAndSendChanges();
    }

    public void setInventoryUpdateListener(Runnable listenerIn) {
        this.inventoryUpdateListener = listenerIn;
    }

    @Override
    public boolean canMergeSlot(@Nonnull ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.inventory && super.canMergeSlot(stack, slotIn);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack currentItemStack = slot.getStack();
            Item item = currentItemStack.getItem();
            itemstack = currentItemStack.copy();
            if (index == 1) {
                item.onCreated(currentItemStack, player.world, player);
                if (!this.mergeItemStack(currentItemStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(currentItemStack, itemstack);
            } else if (index == 0) {
                if (!this.mergeItemStack(currentItemStack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.world.getRecipeManager().getRecipe(this.recipeType, new Inventory(currentItemStack), this.world).isPresent()) {
                if (!this.mergeItemStack(currentItemStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 29) {
                if (!this.mergeItemStack(currentItemStack, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 38 && !this.mergeItemStack(currentItemStack, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if (currentItemStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }

            slot.onSlotChanged();
            if (currentItemStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, currentItemStack);
            this.detectAndSendChanges();
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(@Nonnull PlayerEntity player) {
        super.onContainerClosed(player);
        this.inventory.removeStackFromSlot(1);
        this.worldPosCallable.consume((world, blockPos) ->
                this.clearContainer(player, player.world, this.inputInventory));
    }

    private boolean attemptCraft() {
        T recipe = this.recipes.get(this.selectedRecipe.get());
        ItemStack pulledStack;
        int requiredCount = recipe.getIngredient().getCount();
        if (requiredCount > 1) {
            pulledStack = this.inputInventory.decrStackSize(0, recipe.getIngredient().getCount());
            if (!pulledStack.isEmpty() && pulledStack.getCount() != requiredCount) {
                this.inputInventory.getStackInSlot(0).grow(pulledStack.getCount());
                pulledStack = ItemStack.EMPTY;
            }
        } else {
            ItemStack itemStack = this.inputInventory.getStackInSlot(0);
            if (itemStack.hasContainerItem()) {
                this.inputInventory.setInventorySlotContents(0, itemStack.getContainerItem());
                pulledStack = itemStack;
            } else {
                pulledStack = this.inputInventory.decrStackSize(0, 1);
            }
        }

        this.updateRecipeResultSlot();
        return !pulledStack.isEmpty();
    }
}

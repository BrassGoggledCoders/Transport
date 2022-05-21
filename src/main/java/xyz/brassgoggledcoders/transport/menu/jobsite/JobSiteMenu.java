package xyz.brassgoggledcoders.transport.menu.jobsite;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.recipe.IJobSiteRecipe;

import java.util.List;

public abstract class JobSiteMenu<T extends IJobSiteRecipe<T>> extends AbstractContainerMenu {
    private static final int INPUT_SLOT = 0;
    private static final int SECONDARY_INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;

    private final ContainerLevelAccess access;
    /**
     * The index of the selected recipe in the GUI.
     */
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private final Level level;
    private final List<T> recipes = Lists.newArrayList();

    private final NonNullList<ItemStack> inputs = NonNullList.withSize(2, ItemStack.EMPTY);

    private long lastSoundTime;
    private final List<Slot> inputSlots;
    private final Slot resultSlot;
    private Runnable slotUpdateListener = () -> {
    };
    private final Container container = new SimpleContainer(2) {
        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            super.setChanged();
            JobSiteMenu.this.slotsChanged(this);
            JobSiteMenu.this.slotUpdateListener.run();
        }
    };

    private final ResultContainer resultContainer = new ResultContainer();

    public JobSiteMenu(MenuType<?> menuType, int menuId, Inventory inventory, final ContainerLevelAccess levelAccess) {
        super(menuType, menuId);
        this.access = levelAccess;
        this.level = inventory.player.level;
        this.inputSlots = Lists.newArrayList(
                this.addSlot(new Slot(this.container, 0, 20, 24)),
                this.addSlot(new Slot(this.container, 1, 20, 42))
        );
        this.resultSlot = this.addSlot(new JobSiteResultSlot<>(this.resultContainer, 1, 143, 33, this));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);
    }

    /**
     * Returns the index of the selected recipe.
     */
    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<T> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return !this.recipes.isEmpty() && this.inputSlots.stream().anyMatch(Slot::hasItem);
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(this.access, pPlayer, this.getBlock());
    }

    @Override
    public boolean clickMenuButton(@NotNull Player pPlayer, int pId) {
        if (this.isValidRecipeIndex(pId)) {
            this.selectedRecipeIndex.set(pId);
            this.setupResultSlot();
        }

        return true;
    }

    private boolean isValidRecipeIndex(int recipeIndex) {
        return recipeIndex >= 0 && recipeIndex < this.recipes.size();
    }

    @Override
    public void slotsChanged(@NotNull Container pInventory) {
        boolean changed = false;
        for (int i = 0; i < OUTPUT_SLOT; i++) {
            ItemStack inputSlotStack = this.inputSlots.get(i).getItem();
            ItemStack inputStack = this.inputs.get(i);
            if (!ItemStack.isSame(inputStack, inputSlotStack) || inputSlotStack.getCount() != inputStack.getCount()) {
                this.inputs.set(i, inputSlotStack.copy());
                changed = true;
            }
        }
        if (changed) {
            this.setupRecipeList(pInventory);
        }

    }

    private void setupRecipeList(Container pInventory) {
        int numberRecipes = this.getNumRecipes();
        int lastSelectedSlot = -1;

        T lastRecipe = null;
        if (numberRecipes > 0) {
            lastSelectedSlot = this.selectedRecipeIndex.get();
            if (lastSelectedSlot >= 0) {
                lastRecipe = this.recipes.get(lastSelectedSlot);
            }
        }

        this.recipes.clear();

        if (this.inputSlots.stream().map(Slot::getItem).anyMatch(itemStack -> !itemStack.isEmpty())) {
            this.recipes.addAll(this.level.getRecipeManager()
                    .getRecipesFor(this.getRecipeType(), pInventory, this.level)
            );
        }

        if (lastSelectedSlot < 0 || numberRecipes != this.getNumRecipes() || this.recipes.get(lastSelectedSlot) != lastRecipe) {
            this.selectedRecipeIndex.set(-1);
            this.resultSlot.set(ItemStack.EMPTY);
        }
    }

    public void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            T recipe = this.recipes.get(this.selectedRecipeIndex.get());
            this.resultContainer.setRecipeUsed(recipe);
            this.resultSlot.set(recipe.assemble(this.container));
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    public void registerUpdateListener(Runnable pListener) {
        this.slotUpdateListener = pListener;
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultContainer && super.canTakeItemForPickAll(pStack, pSlot);
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack copiedItemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack slotItemStack = slot.getItem();
            Item item = slotItemStack.getItem();
            copiedItemStack = slotItemStack.copy();
            if (pIndex == 1) {
                item.onCraftedBy(slotItemStack, pPlayer.level, pPlayer);
                if (!this.moveItemStackTo(slotItemStack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotItemStack, copiedItemStack);
            } else if (pIndex == INPUT_SLOT) {
                if (!this.moveItemStackTo(slotItemStack, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.level.getRecipeManager().getRecipeFor(this.getRecipeType(), new SimpleContainer(slotItemStack), this.level).isPresent()) {
                if (!this.moveItemStackTo(slotItemStack, INPUT_SLOT, SECONDARY_INPUT_SLOT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= INV_SLOT_START && pIndex < INV_SLOT_END) {
                if (!this.moveItemStackTo(slotItemStack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= USE_ROW_SLOT_START && pIndex < USE_ROW_SLOT_END && !this.moveItemStackTo(slotItemStack, OUTPUT_SLOT, INV_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItemStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (slotItemStack.getCount() == copiedItemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, slotItemStack);
            this.broadcastChanges();
        }

        return copiedItemStack;
    }

    @Override
    public void removed(@NotNull Player pPlayer) {
        super.removed(pPlayer);
        this.resultContainer.removeItemNoUpdate(OUTPUT_SLOT);
        this.access.execute((level, pos) -> this.clearContainer(pPlayer, this.container));
    }

    public ContainerLevelAccess getLevelAccess() {
        return this.access;
    }

    public long getLastSoundTime() {
        return this.lastSoundTime;
    }

    public void setLastSoundTime(long l) {
        this.lastSoundTime = l;
    }

    protected abstract Block getBlock();

    protected abstract RecipeType<T> getRecipeType();

    public ResultContainer getResultContainer() {
        return this.resultContainer;
    }

    public boolean removeInputs() {
        return this.getSelectedRecipeIndex() >= 0 && this.getRecipes()
                .get(this.getSelectedRecipeIndex())
                .reduceContainer(this.container);
    }

    public Container getContainer() {
        return this.container;
    }
}

package xyz.brassgoggledcoders.transport.recipe.shellitem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Triple;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public record ShellItemRecipe(
        ResourceLocation id,
        Ingredient input,
        ItemStack output,
        Ingredient glue,
        boolean glueOptional
) implements CraftingRecipe {
    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        return findMatching(pContainer)
                .map(tuple -> {
                    for (ShellContentCreatorInfo info : TransportAPI.SHELL_CONTENT_CREATOR.get().getAll()) {
                        if (info.createRecipe() && info.viewState().getBlock().asItem() == tuple.getMiddle().getItem()) {
                            return true;
                        }
                    }
                    return false;
                }).orElse(false);
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull CraftingContainer pContainer) {
        return findMatching(pContainer)
                .map(tuple -> {
                    for (ShellContentCreatorInfo info : TransportAPI.SHELL_CONTENT_CREATOR.get().getAll()) {
                        if (info.createRecipe() && info.viewState().getBlock().asItem() == tuple.getMiddle().getItem()) {
                            ItemStack itemStack = this.output().copy();
                            CompoundTag shellContent = new CompoundTag();
                            shellContent.putString("id", info.id().toString());
                            itemStack.addTagElement("shellContent", shellContent);
                            return itemStack;
                        }
                    }
                    return ItemStack.EMPTY;
                }).orElse(ItemStack.EMPTY);
    }

    @Override

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pHeight * pWidth >= 2;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return output().copy();
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return this.id();
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return TransportRecipes.SHELL_ITEMS.get();
    }

    private Optional<Triple<ItemStack, ItemStack, ItemStack>> findMatching(Container pContainer) {
        ItemStack matchesInput = null;
        ItemStack matchesOther = null;
        ItemStack matchesGlue = null;
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack currentStack = pContainer.getItem(i);
            if (!currentStack.isEmpty()) {
                if (input.test(currentStack)) {
                    if (matchesInput == null) {
                        matchesInput = currentStack;
                    } else {
                        return Optional.empty();
                    }
                } else if (glue != null && glue.test(currentStack)) {
                    if (matchesGlue == null) {
                        matchesGlue = currentStack;
                    } else {
                        return Optional.empty();
                    }
                } else {
                    if (matchesOther == null) {
                        matchesOther = currentStack;
                    } else {
                        return Optional.empty();
                    }
                }
            }
        }
        return matchesInput != null && matchesOther != null && (matchesGlue != null || glueOptional) ?
                Optional.of(Triple.of(matchesInput, matchesOther, matchesGlue)) : Optional.empty();
    }
}
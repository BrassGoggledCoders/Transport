package xyz.brassgoggledcoders.transport.recipe.railworkerbench;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.ingredient.SizedIngredient;

import java.util.Collection;
import java.util.Collections;

public record RailWorkerBenchRecipe(
        ResourceLocation id,
        ItemStack output,
        SizedIngredient input,
        SizedIngredient secondaryInput
) implements IRailWorkerBenchRecipe {
    @Override
    public boolean matches(@NotNull Container pContainer, @NotNull Level pLevel) {
        return (input().test(pContainer.getItem(0)) && secondaryInput().test(pContainer.getItem(1))) ||
                secondaryInput().test(pContainer.getItem(0)) && input().test(pContainer.getItem(1));
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull Container pContainer) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return secondaryInput().isEmpty() || pHeight * pWidth >= 2;
    }

    @Override
    @NotNull
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    @NotNull
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.input().ingredient());
        if (!this.secondaryInput().isEmpty()) {
            ingredients.add(this.secondaryInput().ingredient());
        }
        return ingredients;
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return TransportRecipes.RAIL_WORKER_BENCH.get();
    }

    @Override
    @NotNull
    public RecipeType<?> getType() {
        return TransportRecipes.RAIL_WORKER_BENCH_TYPE.get();
    }

    @Override
    public boolean reduceContainer(Container pContainer) {
        if (input().test(pContainer.getItem(0)) && secondaryInput().test(pContainer.getItem(1))) {
            return handleReduction(input(), pContainer, 0) && handleReduction(secondaryInput(), pContainer, 1);
        } else if (secondaryInput().test(pContainer.getItem(0)) && input().test(pContainer.getItem(1))) {
            return handleReduction(secondaryInput(), pContainer, 0) && handleReduction(input(), pContainer, 1);
        } else {
            return false;
        }
    }

    private boolean handleReduction(SizedIngredient ingredient, Container pContainer, int index) {
        if (ingredient.isEmpty()) {
            return true;
        } else if (ingredient.count() > 1) {
            ItemStack pulledStack = pContainer.removeItem(0, ingredient.count());
            if (!pulledStack.isEmpty() && pulledStack.getCount() != ingredient.count()) {
                pContainer.getItem(index).grow(pulledStack.getCount());
                pulledStack = ItemStack.EMPTY;
            }
            return !pulledStack.isEmpty();
        } else {
            ItemStack itemStack = pContainer.getItem(index);
            if (itemStack.hasContainerItem()) {
                pContainer.setItem(index, itemStack.getContainerItem());
                return true;
            } else {
                return !pContainer.removeItem(index, 1).isEmpty();
            }
        }
    }

    @Override
    public Collection<IRailWorkerBenchRecipe> getChildren() {
        return Collections.singleton(this);
    }

    @Override
    public SizedIngredient getInput() {
        return input();
    }

    @Override
    public SizedIngredient getSecondaryInput() {
        return secondaryInput();
    }
}

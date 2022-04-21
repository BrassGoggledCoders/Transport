package xyz.brassgoggledcoders.transport.recipe.railworkerbench;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

public record RailWorkerBenchRecipe(
        ResourceLocation id,
        ItemStack output,
        Ingredient input,
        Ingredient secondaryInput
) implements Recipe<Container> {
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
        ingredients.add(this.input());
        if (!this.secondaryInput().isEmpty()) {
            ingredients.add(this.secondaryInput());
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
}

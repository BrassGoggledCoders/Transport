package xyz.brassgoggledcoders.transport.data.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import xyz.brassgoggledcoders.transport.api.recipe.ingredient.SizedIngredient;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.RailWorkerBenchRecipe;

import java.util.Objects;

public class RailWorkerBenchRecipeBuilder {
    private static final ResourceLocation NAME = new ResourceLocation("transport:rail_worker_bench");

    private final ItemStack output;
    private SizedIngredient input;
    private SizedIngredient secondaryInput;

    public RailWorkerBenchRecipeBuilder(ItemStack output) {
        this.output = output;
        this.secondaryInput = SizedIngredient.of(Ingredient.EMPTY);
    }

    public RailWorkerBenchRecipeBuilder withInput(Ingredient input) {
        return this.withInput(input, 1);
    }

    public RailWorkerBenchRecipeBuilder withInput(Ingredient input, int count) {
        this.input = SizedIngredient.of(input, count);
        return this;
    }

    public RailWorkerBenchRecipeBuilder withInput(ItemLike input, int count) {
        return this.withInput(Ingredient.of(input), count);
    }

    public RailWorkerBenchRecipeBuilder withInput(TagKey<Item> input, int count) {
        return this.withInput(Ingredient.of(input), count);
    }

    public RailWorkerBenchRecipeBuilder withSecondaryInput(Ingredient secondaryInput) {
        return this.withSecondaryInput(secondaryInput, 1);
    }

    public RailWorkerBenchRecipeBuilder withSecondaryInput(ItemLike secondaryInput) {
        return this.withSecondaryInput(Ingredient.of(secondaryInput), 1);
    }

    public RailWorkerBenchRecipeBuilder withSecondaryInput(Ingredient secondaryInput, int count) {
        this.secondaryInput = SizedIngredient.of(secondaryInput, count);
        return this;
    }

    public RailWorkerBenchRecipeBuilder withSecondaryInput(TagKey<Item> secondInput, int count) {
        return this.withSecondaryInput(Ingredient.of(secondInput), count);
    }

    public void save(RecipeOutput recipeOutput) {
        this.save(recipeOutput, getDefaultRecipeId(this.output.getItem()));
    }

    private static ResourceLocation getDefaultRecipeId(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem());
    }

    public void save(RecipeOutput recipeOutput, ResourceLocation pRecipeId) {
        Objects.requireNonNull(this.input, "input cannot be null");
        if (this.input.isEmpty()) {
            throw new IllegalStateException("input cannot be empty");
        }
        Objects.requireNonNull(this.output, "output cannot be null");
        Objects.requireNonNull(this.secondaryInput, "secondaryInput cannot be null");
        recipeOutput.accept(
                pRecipeId,
                new RailWorkerBenchRecipe(
                        this.output,
                        this.input,
                        this.secondaryInput
                ),
                null);
    }

    public static RailWorkerBenchRecipeBuilder of(ItemLike input) {
        return new RailWorkerBenchRecipeBuilder(new ItemStack(input));
    }

    public static RailWorkerBenchRecipeBuilder of(ItemLike input, int count) {
        return new RailWorkerBenchRecipeBuilder(new ItemStack(input, count));
    }
}

package xyz.brassgoggledcoders.transport.data.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import xyz.brassgoggledcoders.transport.recipe.shellitem.ShellItemRecipe;

public class ShellItemRecipeBuilder {
    private final ItemStack result;
    private Ingredient input;

    public ShellItemRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public ShellItemRecipeBuilder withInput(Ingredient input) {
        this.input = input;
        return this;
    }

    public void save(RecipeOutput pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(pRecipeId, new ShellItemRecipe(input, result), null);
    }

    public static ShellItemRecipeBuilder of(ItemLike input) {
        return new ShellItemRecipeBuilder(new ItemStack(input));
    }
}


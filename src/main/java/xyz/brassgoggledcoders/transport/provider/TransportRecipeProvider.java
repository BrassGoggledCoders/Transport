package xyz.brassgoggledcoders.transport.provider;

import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.tags.ItemTags;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class TransportRecipeProvider extends RecipeProvider {
    public TransportRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        //region Rails
        ShapelessRecipeBuilder.shapelessRecipe(TransportBlocks.HOLDING_RAIL.get(), 2)
                .addIngredient(Items.RAIL)
                .addIngredient(Items.POWERED_RAIL)
                .addCriterion("has_rail", this.hasItem(ItemTags.RAILS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.DIAMOND_CROSSING_RAIL.get(), 5)
                .patternLine(" R ")
                .patternLine("RRR")
                .patternLine(" R ")
                .key('R', Ingredient.fromItems(Items.RAIL))
                .addCriterion("has_rail", this.hasItem(ItemTags.RAILS))
                .build(consumer);
        //endregion
    }

    @Override
    @Nonnull
    public String getName() {
        return "Transport Recipes";
    }
}

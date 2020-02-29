package xyz.brassgoggledcoders.transport.provider;

import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
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
        ShapelessRecipeBuilder.shapelessRecipe(TransportBlocks.HOLDING_RAIL.getItem(), 2)
                .addIngredient(Items.RAIL)
                .addIngredient(Items.POWERED_RAIL)
                .addCriterion("has_rail", this.hasItem(ItemTags.RAILS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.DIAMOND_CROSSING_RAIL.getItem(), 5)
                .patternLine(" R ")
                .patternLine("RRR")
                .patternLine(" R ")
                .key('R', Ingredient.fromItems(Items.RAIL))
                .addCriterion("has_rail", this.hasItem(ItemTags.RAILS))
                .build(consumer);
        //endregion

        //region Loaders
        createLoader(TransportBlocks.ITEM_LOADER.getItem(), Tags.Items.CHESTS)
                .build(consumer);
        createLoader(TransportBlocks.ENERGY_LOADER.getItem(), Tags.Items.DUSTS_REDSTONE)
                .build(consumer);
        createLoader(TransportBlocks.FLUID_LOADER.getItem(), Ingredient.fromItems(Items.BUCKET))
                .addCriterion("has_item", this.hasItem(Items.BUCKET))
                .build(consumer);
        //endregion
    }

    @Override
    @Nonnull
    public String getName() {
        return "Transport Recipes";
    }

    public ShapedRecipeBuilder createLoader(IItemProvider loader, Tag<Item> center) {
        return createLoader(loader, Ingredient.fromTag(center))
                .addCriterion("has_item", this.hasItem(center));
    }

    public ShapedRecipeBuilder createLoader(IItemProvider loader, Ingredient center) {
        return ShapedRecipeBuilder.shapedRecipe(loader)
                .patternLine("III")
                .patternLine("PCP")
                .patternLine("III")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('P', ItemTags.PLANKS)
                .key('C', center);
    }
}

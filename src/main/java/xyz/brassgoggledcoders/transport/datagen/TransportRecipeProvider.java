package xyz.brassgoggledcoders.transport.datagen;

import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportEngineModules;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportItems;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class TransportRecipeProvider extends RecipeProvider {
    public TransportRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        //region Rails
        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.DIAMOND_CROSSING_RAIL.getItem(), 5)
                .patternLine(" R ")
                .patternLine("RRR")
                .patternLine(" R ")
                .key('R', Ingredient.fromItems(Items.RAIL))
                .addCriterion("has_rail", hasItem(ItemTags.RAILS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.ELEVATOR_SWITCH_RAIL.getItem())
                .patternLine("R")
                .patternLine("P")
                .patternLine("S")
                .key('R', Ingredient.fromItems(Items.RAIL))
                .key('P', Tags.Items.DUSTS_REDSTONE)
                .key('S', Ingredient.fromItems(Items.SCAFFOLDING))
                .addCriterion("has_item", hasItem(Items.SCAFFOLDING))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.SCAFFOLDING_RAIL.getItem(), 3)
                .patternLine("RRR")
                .patternLine("SSS")
                .key('R', Ingredient.fromItems(Items.RAIL))
                .key('S', Ingredient.fromItems(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.getItem()))
                .addCriterion("has_item", hasItem(Items.SCAFFOLDING))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.SWITCH_RAIL.getItem(), 4)
                .patternLine("R ")
                .patternLine("RR")
                .patternLine("R ")
                .key('R', Items.RAIL)
                .addCriterion("has_item", hasItem(Items.RAIL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.WYE_SWITCH_RAIL.getItem(), 4)
                .patternLine("RRR")
                .patternLine(" R ")
                .key('R', Items.RAIL)
                .addCriterion("has_item", hasItem(Items.RAIL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.BUMPER_RAIL.getItem(), 3)
                .patternLine("WRW")
                .patternLine("I I")
                .patternLine("TTT")
                .key('W', Tags.Items.DYES_WHITE)
                .key('R', Tags.Items.DYES_RED)
                .key('I', Tags.Items.INGOTS_IRON)
                .key('T', Items.RAIL)
                .addCriterion("has_item", hasItem(Items.RAIL))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(TransportBlocks.TIMED_HOLDING_RAIL.getItem(), 2)
                .addIngredient(Items.RAIL)
                .addIngredient(Items.REPEATER)
                .addCriterion("has_rail", hasItem(ItemTags.RAILS))
                .build(consumer);
        //endregion

        //region Loaders
        createLoader(TransportBlocks.ITEM_LOADER.getItem(), Tags.Items.CHESTS)
                .build(consumer);
        createLoader(TransportBlocks.ENERGY_LOADER.getItem(), Tags.Items.DUSTS_REDSTONE)
                .build(consumer);
        createLoader(TransportBlocks.FLUID_LOADER.getItem(), Ingredient.fromItems(Items.BUCKET))
                .addCriterion("has_item", hasItem(Items.BUCKET))
                .build(consumer);
        //endregion

        //region Entities
        ShapedRecipeBuilder.shapedRecipe(TransportEntities.CARGO_MINECART_ITEM.get())
                .patternLine(" S ")
                .patternLine("RMR")
                .patternLine(" S ")
                .key('S', Ingredient.fromTag(Tags.Items.SLIMEBALLS))
                .key('R', Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE))
                .key('M', Ingredient.fromItems(Items.MINECART))
                .addCriterion("has_item", hasItem(Items.MINECART))
                .build(consumer);
        //endregion

        //region Engines
        ShapedRecipeBuilder.shapedRecipe(TransportEngineModules.BOOSTER_ITEM::get)
                .patternLine("G G")
                .patternLine("RGR")
                .patternLine("G G")
                .key('G', Tags.Items.INGOTS_GOLD)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("has_item", hasItem(Tags.Items.INGOTS_GOLD))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportEngineModules.SOLID_FUEL_ITEM::get)
                .patternLine("F")
                .patternLine("F")
                .key('F', Items.FURNACE)
                .addCriterion("has_item", hasItem(Items.FURNACE))
                .build(consumer);
        //endregion

        //region Blocks
        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.getItem(), 6)
                .patternLine("SSS")
                .key('S', Ingredient.fromItems(Items.SCAFFOLDING))
                .addCriterion("has_item", hasItem(Items.SCAFFOLDING))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.MODULE_CONFIGURATOR.getItem())
                .patternLine("CCC")
                .patternLine("III")
                .key('C', Ingredient.fromItems(Items.CRAFTING_TABLE))
                .key('I', Ingredient.fromTag(Tags.Items.INGOTS_IRON))
                .addCriterion("has_item", hasItem(Items.MINECART))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.BUOY.get())
                .patternLine(" P ")
                .patternLine(" I ")
                .patternLine("IRI")
                .key('P', Tags.Items.GEMS_PRISMARINE)
                .key('I', Tags.Items.INGOTS_IRON)
                .key('R', Tags.Items.DYES_RED)
                .addCriterion("has_item", hasItem(Items.OAK_BOAT));
        //endregion
    }

    @Override
    @Nonnull
    public String getName() {
        return "Transport Recipes";
    }

    public ShapedRecipeBuilder createLoader(IItemProvider loader, ITag.INamedTag<Item> center) {
        return createLoader(loader, Ingredient.fromTag(center))
                .addCriterion("has_item", hasItem(center));
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

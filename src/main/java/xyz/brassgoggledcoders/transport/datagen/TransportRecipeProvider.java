package xyz.brassgoggledcoders.transport.datagen;

import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.NBTIngredient;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.recipe.CargoShapelessRecipeBuilder;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportEngineModules;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportItems;
import xyz.brassgoggledcoders.transport.recipe.ActualNBTIngredient;

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

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.ELEVATOR_SWITCH_RAIL.getItem())
                .patternLine("R")
                .patternLine("P")
                .patternLine("S")
                .key('R', Ingredient.fromItems(Items.RAIL))
                .key('P', Tags.Items.DUSTS_REDSTONE)
                .key('S', Ingredient.fromItems(Items.SCAFFOLDING))
                .addCriterion("has_item", this.hasItem(Items.SCAFFOLDING))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.SCAFFOLDING_RAIL.getItem(), 3)
                .patternLine("RRR")
                .patternLine("SSS")
                .key('R', Ingredient.fromItems(Items.RAIL))
                .key('S', Ingredient.fromItems(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.getItem()))
                .addCriterion("has_item", this.hasItem(Items.SCAFFOLDING))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.SWITCH_RAIL.getItem(), 4)
                .patternLine("R ")
                .patternLine("RR")
                .patternLine("R ")
                .key('R', Items.RAIL)
                .addCriterion("has_item", this.hasItem(Items.RAIL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.WYE_SWITCH_RAIL.getItem(), 4)
                .patternLine("RRR")
                .patternLine(" R ")
                .key('R', Items.RAIL)
                .addCriterion("has_item", this.hasItem(Items.RAIL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.BUMPER_RAIL.getItem(), 3)
                .patternLine("WRW")
                .patternLine("I I")
                .patternLine("TTT")
                .key('W', Tags.Items.DYES_WHITE)
                .key('R', Tags.Items.DYES_RED)
                .key('I', Tags.Items.INGOTS_IRON)
                .key('T', Items.RAIL)
                .addCriterion("has_item", this.hasItem(Items.RAIL))
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

        //region Entities
        ShapedRecipeBuilder.shapedRecipe(TransportEntities.CARGO_MINECART_ITEM.get(), 1)
                .patternLine("r")
                .patternLine("s")
                .patternLine("m")
                .key('r', Tags.Items.DUSTS_REDSTONE)
                .key('s', Tags.Items.SLIMEBALLS)
                .key('m', Items.MINECART)
                .addCriterion("has_item", this.hasItem(Items.MINECART))
                .build(consumer);
        //endregion

        //region Engines
        ShapedRecipeBuilder.shapedRecipe(TransportEngineModules.BOOSTER_ITEM::get)
                .patternLine("G G")
                .patternLine("RGR")
                .patternLine("G G")
                .key('G', Tags.Items.INGOTS_GOLD)
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("has_item", this.hasItem(Tags.Items.INGOTS_GOLD))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportEngineModules.SOLID_FUEL_ITEM::get)
                .patternLine("F")
                .patternLine("F")
                .key('F', Items.FURNACE)
                .addCriterion("has_item", this.hasItem(Items.FURNACE))
                .build(consumer);
        //endregion

        //region Items
        ShapedRecipeBuilder.shapedRecipe(TransportItems.RAIL_BREAKER.get())
                .patternLine(" RI")
                .patternLine("RIR")
                .patternLine("IR ")
                .key('R', Tags.Items.DYES_RED)
                .key('I', Tags.Items.INGOTS_IRON)
                .addCriterion("has_item", this.hasItem(Tags.Items.INGOTS_IRON))
                .build(consumer);
        //endregion

        //region Blocks
        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.getItem(), 6)
                .patternLine("SSS")
                .key('S', Ingredient.fromItems(Items.SCAFFOLDING))
                .addCriterion("has_item", this.hasItem(Items.SCAFFOLDING))
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

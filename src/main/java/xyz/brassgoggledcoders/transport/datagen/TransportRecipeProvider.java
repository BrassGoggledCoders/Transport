package xyz.brassgoggledcoders.transport.datagen;

import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportEngineModules;
import xyz.brassgoggledcoders.transport.content.TransportEntities;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class TransportRecipeProvider extends RecipeProvider {
    public TransportRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
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

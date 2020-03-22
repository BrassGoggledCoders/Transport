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
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.recipe.CargoShapelessRecipeBuilder;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportEntities;

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
                .key('S', Ingredient.fromItems(Items.SCAFFOLDING))
                .addCriterion("has_item", this.hasItem(Items.SCAFFOLDING))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.SWITCH_RAIL.getItem(), 4)
                .patternLine("R ")
                .patternLine("RR")
                .patternLine("R ")
                .key('R', Items.RAIL)
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

        //region Cargo
        TransportAPI.CARGO.getValues().forEach(cargo -> {
            if (cargo.asItem() != Items.AIR && cargo.getRegistryName() != null) {
                ResourceLocation registryName = cargo.getRegistryName();
                CargoShapelessRecipeBuilder.start(TransportEntities.CARGO_MINECART_ITEM.get(), cargo)
                        .addIngredient(Tags.Items.SLIMEBALLS)
                        .addIngredient(Items.MINECART)
                        .addIngredient(cargo.asItem())
                        .addCriterion("has_item", hasItem(cargo.asItem()))
                        .build(consumer, new ResourceLocation(registryName.getNamespace(), "cargo/minecart/" +
                                registryName.getPath()));
                ShapelessRecipeBuilder.shapelessRecipe(cargo.asItem())
                        .addIngredient(Items.WATER_BUCKET)
                        .addIngredient(Ingredient.fromStacks(cargo.createItemStack(TransportEntities.CARGO_MINECART_ITEM.get())))
                        .addCriterion("has_item", hasItem(TransportEntities.CARGO_MINECART_ITEM.get()))
                        .build(consumer, new ResourceLocation(registryName.getNamespace(), "cargo/minecart/" +
                                registryName.getPath() + "_break"));
            }
        });
        //endregion

        //region Blocks
        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.getItem(), 6)
                .patternLine("SSS")
                .key('S', Ingredient.fromItems(Items.SCAFFOLDING))
                .addCriterion("has_item", this.hasItem(Items.SCAFFOLDING))
                .build(consumer);
        //endregion

        //region Switch Motors
        ShapedRecipeBuilder.shapedRecipe(TransportBlocks.REDSTONE_SWITCH_MOTOR.getItem())
                .patternLine("L")
                .patternLine("R")
                .key('L', Items.LEVER)
                .key('R', Items.REPEATER)
                .addCriterion("has_item", this.hasItem(Items.REPEATER))
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

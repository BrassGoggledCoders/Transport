package xyz.brassgoggledcoders.transport.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.registries.IForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipeBuilder;

import javax.annotation.Nullable;

public class TransportRegistrateRecipes {
    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> vehicleShape(
            ITag.INamedTag<Item> material) {
        return (context, recipeProvider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                .patternLine("M M")
                .patternLine("MMM")
                .key('M', material)
                .addCriterion("has_item", RegistrateRecipeProvider.hasItem(material))
                .build(recipeProvider);
    }

    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> vehicleShape(
            String tagName) {
        return (context, recipeProvider) -> {
            ITag.INamedTag<Item> material = ItemTags.makeWrapperTag(tagName);
            ConditionalRecipe.builder()
                    .addCondition(new NotCondition(new TagEmptyCondition(tagName)))
                    .addRecipe(ShapedRecipeBuilder.shapedRecipe(context.get())
                            .patternLine("M M")
                            .patternLine("MMM")
                            .key('M', material)
                            .addCriterion("has_item", RegistrateRecipeProvider.hasItem(material))::build)
                    .build(recipeProvider, context.getId());
        };
    }

    public static <T extends IItemProvider & IForgeRegistryEntry<T>, I extends T> NonNullBiConsumer<DataGenContext<T, I>,
            RegistrateRecipeProvider> createLoader(ITag.INamedTag<Item> center) {
        return (context, provider) -> createLoaderBuilder(context, Ingredient.fromTag(center))
                .addCriterion("has_item", RegistrateRecipeProvider.hasItem(center))
                .build(provider);
    }

    public static <T extends IItemProvider & IForgeRegistryEntry<T>, I extends T> NonNullBiConsumer<DataGenContext<T, I>,
            RegistrateRecipeProvider> createLoader(IItemProvider center) {
        return (context, provider) -> createLoaderBuilder(context, Ingredient.fromItems(center))
                .addCriterion("has_item", RegistrateRecipeProvider.hasItem(center))
                .build(provider);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<T>, I extends T> ShapedRecipeBuilder createLoaderBuilder(
            DataGenContext<T, I> context, Ingredient center) {
        return ShapedRecipeBuilder.shapedRecipe(context.get())
                .patternLine("III")
                .patternLine("PCP")
                .patternLine("III")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('P', ItemTags.PLANKS)
                .key('C', center);
    }

    public static <T extends IItemProvider & IForgeRegistryEntry<T>, I extends T> NonNullBiConsumer<DataGenContext<T, I>,
            RegistrateRecipeProvider> slab(IItemProvider item) {
        return (context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                .patternLine("SSS")
                .key('S', Ingredient.fromItems(item))
                .addCriterion("has_item", RegistrateRecipeProvider.hasItem(item))
                .build(provider);
    }

    public static <T extends IItemProvider & IForgeRegistryEntry<T>, I extends T> NonNullBiConsumer<DataGenContext<T, I>,
            RegistrateRecipeProvider> dualSlab(ITag<Item> bottom, Ingredient top) {
        return (context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                .patternLine("TTT")
                .patternLine("BBB")
                .key('T', top)
                .key('B', bottom)
                .addCriterion("has_item", RegistrateRecipeProvider.hasItem(bottom))
                .build(provider);
    }

    public static <T extends IItemProvider & IForgeRegistryEntry<T>, I extends T> NonNullBiConsumer<DataGenContext<T, I>,
            RegistrateRecipeProvider> railRecipes(ITag.INamedTag<Item> ingot, Item rail) {
        return (context, provider) -> {
            RailWorkerBenchRecipeBuilder.create(ingot, context.get())
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(ingot))
                    .build(provider, Transport.rl(fixRL(context.get().getRegistryName()) +
                            "_from_" + fixRL(ingot.getName())));

            ShapedRecipeBuilder.shapedRecipe(rail, 32)
                    .patternLine("R R")
                    .patternLine("RSR")
                    .patternLine("R R")
                    .key('R', context.get())
                    .key('S', Tags.Items.RODS_WOODEN)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(context.get()))
                    .build(provider, Transport.rl(fixRL(rail.getRegistryName()) +
                            "_from_" + fixRL(context.getId())));
        };
    }

    public static String fixRL(@Nullable ResourceLocation resourceLocation) {
        return resourceLocation != null ? resourceLocation.toString().replace(":", "_")
                .replace("/", "_") : "";
    }
}

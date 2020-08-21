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
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistrateRecipes {
    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> vehicleShape(ITag.INamedTag<Item> material) {
        return (context, recipeProvider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                .patternLine("M M")
                .patternLine("MMM")
                .key('M', material)
                .addCriterion("has_item", RegistrateRecipeProvider.hasItem(material))
                .build(recipeProvider);
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
}

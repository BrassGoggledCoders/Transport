package xyz.brassgoggledcoders.transport.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;

import java.util.function.Supplier;

public class RegistrateRecipes {
    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider> vehicleShape(ITag.INamedTag<Item> material) {
        return (context, recipeProvider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                .patternLine("M M")
                .patternLine("MMM")
                .key('M', material)
                .addCriterion("has_item", RegistrateRecipeProvider.hasItem(material))
                .build(recipeProvider);
    }
}

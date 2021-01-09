package xyz.brassgoggledcoders.transport.recipe.jobsite;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.SizedIngredient;

public class RailWorkerBenchRecipeBuilder extends SingleItemSizedRecipeBuilder {
    public RailWorkerBenchRecipeBuilder(SizedIngredient ingredient, ItemStack result) {
        super(TransportRecipes.RAIL_WORKER_BENCH_SERIALIZER.get(), ingredient, result);
    }

    public static RailWorkerBenchRecipeBuilder create(Ingredient ingredient, IItemProvider resultProvider, int count) {
        return new RailWorkerBenchRecipeBuilder(SizedIngredient.of(ingredient), new ItemStack(resultProvider, count));
    }

    public static RailWorkerBenchRecipeBuilder create(ITag<Item> tag, IItemProvider resultProvider) {
        return new RailWorkerBenchRecipeBuilder(SizedIngredient.of(Ingredient.fromTag(tag)), new ItemStack(resultProvider));
    }

    public static RailWorkerBenchRecipeBuilder create(SizedIngredient sizedIngredient, IItemProvider resultProvider) {
        return new RailWorkerBenchRecipeBuilder(sizedIngredient, new ItemStack(resultProvider));
    }
}

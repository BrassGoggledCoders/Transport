package xyz.brassgoggledcoders.transport.recipe.jobsite;

import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

public class RailWorkerBenchRecipeBuilder extends SingleItemRecipeBuilder {
    public RailWorkerBenchRecipeBuilder(Ingredient ingredient, IItemProvider resultProvider, int count) {
        super(TransportRecipes.RAIL_WORKER_BENCH_SERIALIZER.get(), ingredient, resultProvider, count);
    }

    public static RailWorkerBenchRecipeBuilder create(Ingredient ingredient, IItemProvider resultProvider, int count) {
        return new RailWorkerBenchRecipeBuilder(ingredient, resultProvider, count);
    }

    public static RailWorkerBenchRecipeBuilder create(Ingredient ingredient, IItemProvider resultProvider) {
        return new RailWorkerBenchRecipeBuilder(ingredient, resultProvider, 1);
    }

    public static RailWorkerBenchRecipeBuilder create(ITag<Item> tag, IItemProvider resultProvider) {
        return new RailWorkerBenchRecipeBuilder(Ingredient.fromTag(tag), resultProvider, 1);
    }
}

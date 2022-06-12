package xyz.brassgoggledcoders.transport.recipe.railworkerbench;

import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.IJobSiteRecipe;
import xyz.brassgoggledcoders.transport.recipe.ingredient.SizedIngredient;

public interface IRailWorkerBenchRecipe extends IJobSiteRecipe<IRailWorkerBenchRecipe> {
    SizedIngredient getInput();

    SizedIngredient getSecondaryInput();

    @Override
    @NotNull
    default RecipeType<?> getType() {
        return TransportRecipes.RAIL_WORKER_BENCH_TYPE.get();
    }
}

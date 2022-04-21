package xyz.brassgoggledcoders.transport.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import java.util.function.Consumer;

public class RailWorkerBenchRecipeBuilder {
    private final ItemStack output;
    private Ingredient input;
    private Ingredient secondaryInput;

    public RailWorkerBenchRecipeBuilder(ItemStack output) {
        this.output = output;
    }

    public RailWorkerBenchRecipeBuilder withInput(Ingredient input) {
        this.input = input;
        return this;
    }

    public RailWorkerBenchRecipeBuilder withSecondaryInput(Ingredient secondaryInput) {
        this.secondaryInput = secondaryInput;
        return this;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new RailWorkerBenchFinishedRecipe(
                pRecipeId,
                this.output,
                this.input,
                this.secondaryInput
        ));
    }

    public static RailWorkerBenchRecipeBuilder of(ItemLike input) {
        return new RailWorkerBenchRecipeBuilder(new ItemStack(input));
    }

    public static RailWorkerBenchRecipeBuilder of(ItemLike input, int count) {
        return new RailWorkerBenchRecipeBuilder(new ItemStack(input, count));
    }

    public static class RailWorkerBenchFinishedRecipe extends BasicFinishedRecipe {
        private final Ingredient secondaryInput;

        public RailWorkerBenchFinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input, Ingredient secondaryInput) {
            super(id, output, input, TransportRecipes.RAIL_WORKER_BENCH.get());
            this.secondaryInput = secondaryInput;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            super.serializeRecipeData(pJson);
            if (!secondaryInput.isEmpty()) {
                pJson.add("secondaryInput", secondaryInput.toJson());
            }
        }
    }
}

package xyz.brassgoggledcoders.transport.data.recipe;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.recipe.ingredient.SizedIngredient;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RailWorkerBenchRecipeBuilder {
    private static final ResourceLocation NAME = new ResourceLocation("transport:rail_worker_bench");
    private static final Supplier<RecipeSerializer<?>> RECIPE_SERIALIZER = Suppliers.memoize(() ->
            Optional.ofNullable(ForgeRegistries.RECIPE_SERIALIZERS.getValue(NAME))
                    .orElseThrow()
    );

    private final ItemStack output;
    private SizedIngredient input;
    private SizedIngredient secondaryInput;

    public RailWorkerBenchRecipeBuilder(ItemStack output) {
        this.output = output;
        this.secondaryInput = SizedIngredient.of(Ingredient.EMPTY);
    }

    public RailWorkerBenchRecipeBuilder withInput(Ingredient input) {
        return this.withInput(input, 1);
    }

    public RailWorkerBenchRecipeBuilder withInput(Ingredient input, int count) {
        this.input = SizedIngredient.of(input, count);
        return this;
    }

    public RailWorkerBenchRecipeBuilder withSecondaryInput(Ingredient secondaryInput) {
        return this.withSecondaryInput(secondaryInput, 1);
    }

    public RailWorkerBenchRecipeBuilder withSecondaryInput(Ingredient secondaryInput, int count) {
        this.secondaryInput = SizedIngredient.of(secondaryInput, count);
        return this;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        this.save(pFinishedRecipeConsumer, getDefaultRecipeId(this.output.getItem()));
    }

    private static ResourceLocation getDefaultRecipeId(ItemLike pItemLike) {
        return ForgeRegistries.ITEMS.getKey(pItemLike.asItem());
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        Objects.requireNonNull(this.input, "input cannot be null");
        Objects.requireNonNull(this.output, "output cannot be null");
        Objects.requireNonNull(this.secondaryInput, "secondaryInput cannot be null");
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
        private final SizedIngredient secondaryInput;

        public RailWorkerBenchFinishedRecipe(ResourceLocation id, ItemStack output, SizedIngredient input, SizedIngredient secondaryInput) {
            super(id, output, input, RECIPE_SERIALIZER.get());
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

package xyz.brassgoggledcoders.transport.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;

public record ShellItemRecipeBuilder(
        ItemStack result,
        Ingredient ingredient
) {
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new ShellItemFinishedRecipe(pRecipeId, this.result, this.ingredient));
    }

    public record ShellItemFinishedRecipe(
            ResourceLocation id,
            ItemStack result,
            Ingredient ingredient
    ) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(@Nonnull JsonObject pJson) {
            pJson.add("input", ingredient.toJson());
            pJson.add("output", serializeItemStack(result));
        }

        @Override
        @Nonnull
        public ResourceLocation getId() {
            return this.id();
        }

        @Override
        @Nonnull
        public RecipeSerializer<?> getType() {
            return TransportRecipes.SHELL_ITEMS.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }


        public JsonObject serializeItemStack(ItemStack stack) {
            JsonObject obj = new JsonObject();
            obj.addProperty("item", Objects.requireNonNull(stack.getItem().getRegistryName()).toString());
            if (stack.getCount() > 1) {
                obj.addProperty("count", stack.getCount());
            }
            if (stack.hasTag()) {
                obj.addProperty("nbt", Objects.requireNonNull(stack.getTag()).toString());
            }
            return obj;
        }
    }
}


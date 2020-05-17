package xyz.brassgoggledcoders.transport.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CargoRecipeSerializer extends ShapelessRecipe.Serializer {
    private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (int i = 0; i < jsonArray.size(); ++i) {
            Ingredient ingredient = Ingredient.deserialize(jsonArray.get(i));
            if (!ingredient.hasNoMatchingItems()) {
                ingredients.add(ingredient);
            }
        }

        return ingredients;
    }

    @Override
    @Nonnull
    public ShapelessRecipe read(@Nonnull ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
        NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
        if (ingredients.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (ingredients.size() > 9) {
            throw new JsonParseException("Too many ingredients for shapeless recipe the max is 9");
        } else {
            JsonObject result = JSONUtils.getJsonObject(json, "result");
            CargoModule cargoModule;
            if (result.has("cargo")) {
                ResourceLocation cargoName = new ResourceLocation(result.get("cargo").getAsString());
                cargoModule = TransportAPI.getCargo(cargoName);
                if (cargoModule == null) {
                    throw new JsonParseException("Failed to find Cargo for name: " + cargoName);
                }
            } else {
                throw new JsonParseException("Failed to find Cargo String on Result");
            }
            ItemStack itemStack = ShapedRecipe.deserializeItem(result);
            itemStack.getOrCreateChildTag("cargo").putString("name", Objects.requireNonNull(
                    cargoModule.getRegistryName()).toString());
            return new ShapelessRecipe(recipeId, s, itemStack, ingredients);
        }
    }
}

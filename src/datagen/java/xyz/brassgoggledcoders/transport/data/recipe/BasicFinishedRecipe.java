package xyz.brassgoggledcoders.transport.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BasicFinishedRecipe implements FinishedRecipe {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;

    private final RecipeSerializer<?> recipeSerializer;

    public BasicFinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input, RecipeSerializer<?> recipeSerializer) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.recipeSerializer = recipeSerializer;
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
        pJson.add("input", input.toJson());
        pJson.add("output", serializeItemStack(output));
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getType() {
        return recipeSerializer;
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

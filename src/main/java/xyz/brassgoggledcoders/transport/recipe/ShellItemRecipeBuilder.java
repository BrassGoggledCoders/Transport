package xyz.brassgoggledcoders.transport.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;

public class ShellItemRecipeBuilder {
    private final ItemStack result;
    private Ingredient input;
    private Ingredient glue;
    private boolean glueOptional;

    public ShellItemRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public ShellItemRecipeBuilder withInput(Ingredient input) {
        this.input = input;
        return this;
    }

    public ShellItemRecipeBuilder withGlue(Ingredient glue) {
        this.glue = glue;
        return this;
    }

    public ShellItemRecipeBuilder withGlueOptional(boolean glueOptional) {
        this.glueOptional = glueOptional;
        return this;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new ShellItemFinishedRecipe(
                pRecipeId,
                this.result,
                this.input,
                this.glue,
                this.glueOptional
        ));
    }

    public static ShellItemRecipeBuilder of(ItemLike input) {
        return new ShellItemRecipeBuilder(new ItemStack(input));
    }

    public record ShellItemFinishedRecipe(
            ResourceLocation id,
            ItemStack result,
            Ingredient ingredient,
            Ingredient glue,
            boolean glueRequired
    ) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(@Nonnull JsonObject pJson) {
            pJson.add("input", ingredient.toJson());
            pJson.add("output", serializeItemStack(result));
            if (glue != null) {
                pJson.add("glue", glue.toJson());
                pJson.addProperty("glueOptional", glueRequired);
            }
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


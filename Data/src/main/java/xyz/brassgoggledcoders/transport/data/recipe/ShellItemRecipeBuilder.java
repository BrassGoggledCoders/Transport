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
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ShellItemRecipeBuilder {
    private static final ResourceLocation NAME = new ResourceLocation("transport:shell_items");
    private static final Supplier<RecipeSerializer<?>> RECIPE_SERIALIZER = Suppliers.memoize(() ->
            Optional.ofNullable(ForgeRegistries.RECIPE_SERIALIZERS.getValue(NAME))
                    .orElseThrow()
    );

    private final ItemStack result;
    private Ingredient input;

    public ShellItemRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public ShellItemRecipeBuilder withInput(Ingredient input) {
        this.input = input;
        return this;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new ShellItemFinishedRecipe(pRecipeId, this.result, this.input));
    }

    public static ShellItemRecipeBuilder of(ItemLike input) {
        return new ShellItemRecipeBuilder(new ItemStack(input));
    }

    public record ShellItemFinishedRecipe(ResourceLocation id, ItemStack result,
                                          Ingredient ingredient) implements FinishedRecipe {

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
            return RECIPE_SERIALIZER.get();
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


package xyz.brassgoggledcoders.transport.recipe.jobsite;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.recipe.SizedIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class SingleItemSizedRecipeBuilder {
    private final ItemStack result;
    private final SizedIngredient ingredient;
    private final IRecipeSerializer<?> serializer;

    private Advancement.Builder advancementBuilder = null;

    public SingleItemSizedRecipeBuilder(IRecipeSerializer<?> serializerIn, SizedIngredient ingredient, ItemStack result) {
        this.serializer = serializerIn;
        this.result = result;
        this.ingredient = ingredient;
    }

    public SingleItemSizedRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
        if (this.advancementBuilder == null) {
            this.advancementBuilder = Advancement.Builder.builder();
        }
        this.advancementBuilder.withCriterion(name, criterionIn);
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer, String save) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result.getItem());
        if ((new ResourceLocation(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Single Item Sized Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(consumer, new ResourceLocation(save));
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        if (this.advancementBuilder != null) {
            this.advancementBuilder.withParentId(new ResourceLocation("recipes/root"))
                    .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id))
                    .withRewards(AdvancementRewards.Builder.recipe(id))
                    .withRequirementsStrategy(IRequirementsStrategy.OR);
        }

        consumer.accept(new Result(id, this.serializer, "", this.ingredient, this.result,
                this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" +
                this.getGroupPath(this.result) + id.getPath())));
    }

    private String getGroupPath(ItemStack itemStack) {
        ItemGroup itemGroup = itemStack.getItem().getGroup();
        if (itemGroup != null) {
            return itemGroup.getPath() + "/";
        } else {
            return "";
        }
    }

    private void validate(ResourceLocation id) {
        if (this.advancementBuilder != null && this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
        if (this.result.isEmpty()) {
            throw new IllegalStateException("Recipe has empty output");
        }
    }

    public static class Result implements IFinishedRecipe {
        private static final Gson GSON = new Gson();

        private final ResourceLocation id;
        private final String group;
        private final SizedIngredient ingredient;
        private final ItemStack result;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<?> serializer;

        public Result(ResourceLocation id, IRecipeSerializer<?> serializer, String group, SizedIngredient ingredient,
                      ItemStack result, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
            this.id = id;
            this.serializer = serializer;
            this.group = group;
            this.ingredient = ingredient;
            this.result = result;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        public void serialize(@Nonnull JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.add("ingredient", this.ingredient.toJson());

            JsonObject resultElement = new JsonObject();
            resultElement.addProperty("item", Objects.requireNonNull(this.result.getItem().getRegistryName()).toString());
            resultElement.addProperty("count", this.result.getCount());
            if (this.result.getTag() != null) {
                resultElement.add("nbt", GSON.fromJson(this.result.getTag().toString(), JsonObject.class));
            }
            json.add("result", resultElement);
        }

        @Nonnull
        public ResourceLocation getID() {
            return this.id;
        }

        @Nonnull
        public IRecipeSerializer<?> getSerializer() {
            return this.serializer;
        }

        @Nullable
        public JsonObject getAdvancementJson() {
            return this.advancementBuilder.serialize();
        }

        @Nullable
        public ResourceLocation getAdvancementID() {
            return this.advancementId;
        }
    }
}

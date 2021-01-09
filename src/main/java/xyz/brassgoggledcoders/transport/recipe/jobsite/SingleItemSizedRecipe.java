package xyz.brassgoggledcoders.transport.recipe.jobsite;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.recipe.SizedIngredient;

import javax.annotation.Nonnull;

public abstract class SingleItemSizedRecipe implements IRecipe<IInventory> {
    private final SizedIngredient ingredient;
    private final ItemStack result;
    private final IRecipeType<?> type;
    private final IRecipeSerializer<?> serializer;
    private final ResourceLocation id;
    private final String group;

    public SingleItemSizedRecipe(IRecipeType<?> type, IRecipeSerializer<?> serializer, ResourceLocation id, String group,
                                 SizedIngredient ingredient, ItemStack result) {
        this.type = type;
        this.serializer = serializer;
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Nonnull
    public IRecipeType<?> getType() {
        return this.type;
    }

    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Nonnull
    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull
    public String getGroup() {
        return this.group;
    }

    @Nonnull
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Nonnull
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.ingredient.getInternal());
        return ingredients;
    }

    public boolean canFit(int width, int height) {
        return true;
    }

    @Nonnull
    public ItemStack getCraftingResult(@Nonnull IInventory inv) {
        return this.result.copy();
    }

    public SizedIngredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }
}

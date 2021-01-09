package xyz.brassgoggledcoders.transport.recipe.jobsite;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Function4;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.recipe.SizedIngredient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SingleItemSizedRecipeSerializer<T extends SingleItemSizedRecipe> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
    private final Function4<ResourceLocation, String, SizedIngredient, ItemStack, T> factory;

    public SingleItemSizedRecipeSerializer(Function4<ResourceLocation, String, SizedIngredient, ItemStack, T> factory) {
        this.factory = factory;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public T read(ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
        SizedIngredient ingredient = SizedIngredient.fromJson(json.get("ingredient"));
        JsonElement resultElement = json.get("result");
        ItemStack result;
        if (resultElement.isJsonObject()) {
            result = CraftingHelper.getItemStack(resultElement.getAsJsonObject(), true);
        } else {
            String itemName = JSONUtils.getString(json, "result");
            int count = JSONUtils.getInt(json, "count", 1);
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            if (item == null) {
                throw new JsonSyntaxException("Unknown item '" + itemName + "'");
            } else {
                result = new ItemStack(item, count);
            }
        }

        return this.factory.apply(recipeId, s, ingredient, result);
    }

    public T read(@Nonnull ResourceLocation recipeId, PacketBuffer buffer) {
        String group = buffer.readString(32767);
        SizedIngredient ingredient = SizedIngredient.fromBuffer(buffer);
        ItemStack itemstack = buffer.readItemStack();
        return this.factory.apply(recipeId, group, ingredient, itemstack);
    }

    public void write(PacketBuffer buffer, T recipe) {
        buffer.writeString(recipe.getGroup());
        recipe.getIngredient().toBuffer(buffer);
        buffer.writeItemStack(recipe.getResult());
    }
}

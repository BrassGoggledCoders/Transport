package xyz.brassgoggledcoders.transport.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.JSONUtils;

import java.util.List;
import java.util.function.Predicate;

public class SizedIngredient implements Predicate<ItemStack> {
    private final Ingredient ingredient;
    private final int count;

    public SizedIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return itemStack.getCount() >= count && ingredient.test(itemStack);
    }

    public Ingredient getInternal() {
        return ingredient;
    }

    public int getCount() {
        return this.count;
    }

    public List<ItemStack> getMatchingStacks() {
        List<ItemStack> matchingStacks = Lists.newArrayList();
        for (ItemStack itemStack : this.getInternal().getMatchingStacks()) {
            ItemStack copy = itemStack.copy();
            copy.setCount(count);
            matchingStacks.add(copy);
        }
        return matchingStacks;
    }

    public void toBuffer(PacketBuffer packetBuffer) {
        ingredient.write(packetBuffer);
        packetBuffer.writeInt(count);
    }

    public JsonElement toJson() {
        JsonElement ingredientJson = ingredient.serialize();
        if (ingredientJson.isJsonObject()) {
            JsonObject ingredientJsonObject = ingredientJson.getAsJsonObject();
            if (count > 1) {
                ingredientJsonObject.addProperty("count", count);
            }
            return ingredientJsonObject;
        } else if (ingredientJson.isJsonArray()) {
            if (count == 1) {
                return ingredientJson;
            } else {
                throw new IllegalArgumentException("Failed to properly add count to json: " + ingredientJson);
            }
        } else {
            throw new IllegalArgumentException("Failed to properly serialize to json: " + ingredientJson.toString());
        }
    }

    public static SizedIngredient fromBuffer(PacketBuffer packetBuffer) {
        return new SizedIngredient(Ingredient.read(packetBuffer), packetBuffer.readInt());
    }

    public static SizedIngredient fromJson(JsonElement json) {
        return new SizedIngredient(Ingredient.deserialize(json), json.isJsonObject() ?
                JSONUtils.getInt(json.getAsJsonObject(), "count", 1) : 1);
    }

    public static SizedIngredient of(Ingredient ingredient) {
        return new SizedIngredient(ingredient, 1);
    }

    public static SizedIngredient of(Ingredient ingredient, int count) {
        return new SizedIngredient(ingredient, count);
    }

    public static SizedIngredient of(ITag<Item> ingredient, int count) {
        return new SizedIngredient(Ingredient.fromTag(ingredient), count);
    }
}

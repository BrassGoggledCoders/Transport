package xyz.brassgoggledcoders.transport.api.recipe.ingredient;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public record SizedIngredient(
        Ingredient ingredient,
        int count
) implements Predicate<ItemStack> {

    public static final Codec<SizedIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(SizedIngredient::ingredient),
            Codec.intRange(1, 64).optionalFieldOf("count", 1).forGetter(SizedIngredient::count)
    ).apply(instance, SizedIngredient::new));

    @Override
    public boolean test(ItemStack itemStack) {
        return (itemStack.getCount() >= count || ingredient.isEmpty()) && ingredient.test(itemStack);
    }

    public Ingredient getInternal() {
        return ingredient;
    }

    public boolean isEmpty() {
        return this.ingredient.isEmpty() || count() < 1;
    }

    public List<ItemStack> getMatchingStacks() {
        List<ItemStack> matchingStacks = Lists.newArrayList();
        for (ItemStack itemStack : this.getInternal().getItems()) {
            ItemStack copy = itemStack.copy();
            copy.setCount(count);
            matchingStacks.add(copy);
        }
        return matchingStacks;
    }

    public void toNetwork(FriendlyByteBuf packetBuffer) {
        ingredient.toNetwork(packetBuffer);
        packetBuffer.writeInt(count);
    }

    public static SizedIngredient fromNetwork(FriendlyByteBuf packetBuffer) {
        return new SizedIngredient(Ingredient.fromNetwork(packetBuffer), packetBuffer.readInt());
    }

    public static SizedIngredient of(Ingredient ingredient) {
        return new SizedIngredient(ingredient, ingredient.isEmpty() ? 0 : 1);
    }

    public static SizedIngredient of(Ingredient ingredient, int count) {
        return new SizedIngredient(ingredient, count);
    }

    public static SizedIngredient of(TagKey<Item> ingredient, int count) {
        return new SizedIngredient(Ingredient.of(ingredient), count);
    }
}

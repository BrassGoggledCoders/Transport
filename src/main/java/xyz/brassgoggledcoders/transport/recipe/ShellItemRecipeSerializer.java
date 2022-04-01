package xyz.brassgoggledcoders.transport.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ShellItemRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShellItemRecipe> {
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ShellItemRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        return new ShellItemRecipe(
                pRecipeId,
                Ingredient.fromJson(pSerializedRecipe.get("input")),
                CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"), true),
                pSerializedRecipe.has("glue") ? Ingredient.fromJson(pSerializedRecipe.get("glue")) : null,
                !pSerializedRecipe.has("glue") || GsonHelper.getAsBoolean(pSerializedRecipe, "glueOptional", false)
        );
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public ShellItemRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        return new ShellItemRecipe(
                pRecipeId,
                Ingredient.fromNetwork(pBuffer),
                pBuffer.readItem(),
                pBuffer.readBoolean() ? Ingredient.fromNetwork(pBuffer) : null,
                pBuffer.readBoolean()
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void toNetwork(FriendlyByteBuf pBuffer, ShellItemRecipe pRecipe) {
        pRecipe.input().toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.output());
        pBuffer.writeBoolean(pRecipe.glue() != null);
        if (pRecipe.glue() != null) {
            pRecipe.glue().toNetwork(pBuffer);
        }
        pBuffer.writeBoolean(pRecipe.glueOptional());
    }
}

package xyz.brassgoggledcoders.transport.recipe.shellitem;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ShellItemRecipeSerializer implements RecipeSerializer<ShellItemRecipe> {
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ShellItemRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        return new ShellItemRecipe(
                pRecipeId,
                Ingredient.fromJson(pSerializedRecipe.get("input")),
                CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"), true)
        );
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public ShellItemRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        return new ShellItemRecipe(
                pRecipeId,
                Ingredient.fromNetwork(pBuffer),
                pBuffer.readItem()
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void toNetwork(FriendlyByteBuf pBuffer, ShellItemRecipe pRecipe) {
        pRecipe.getInput().ingredient().toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.getOutput());
    }
}

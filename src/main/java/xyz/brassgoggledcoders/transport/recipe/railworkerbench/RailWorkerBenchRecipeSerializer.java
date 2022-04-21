package xyz.brassgoggledcoders.transport.recipe.railworkerbench;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RailWorkerBenchRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RailWorkerBenchRecipe> {
    @Override
    @NotNull
    public RailWorkerBenchRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
        return new RailWorkerBenchRecipe(
                pRecipeId,
                CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"), true),
                Ingredient.fromJson(pSerializedRecipe.get("input")),
                pSerializedRecipe.has("secondaryInput") ? Ingredient.fromJson(pSerializedRecipe.get("secondaryInput")) : Ingredient.EMPTY
        );
    }

    @Nullable
    @Override
    public RailWorkerBenchRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        return new RailWorkerBenchRecipe(
                pRecipeId,
                pBuffer.readItem(),
                Ingredient.fromNetwork(pBuffer),
                Ingredient.fromNetwork(pBuffer)
        );
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull RailWorkerBenchRecipe pRecipe) {
        pBuffer.writeItem(pRecipe.output());
        pRecipe.input().toNetwork(pBuffer);
        pRecipe.secondaryInput().toNetwork(pBuffer);
    }
}

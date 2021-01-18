package xyz.brassgoggledcoders.transport.recipe.module;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.api.module.Module;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ModuleRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<ModuleRecipe> {
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ModuleRecipe read(ResourceLocation recipeId, JsonObject json) {
        return new ModuleRecipe(
                recipeId,
                Module.fromJson(json.get("module")),
                CraftingHelper.getIngredient(json.get("item"))
        );
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public ModuleRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        return new ModuleRecipe(
                recipeId,
                Module.fromPacketBuffer(buffer),
                Ingredient.read(buffer)
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void write(PacketBuffer buffer, ModuleRecipe recipe) {
        Module.toPacketBuffer(recipe.getModuleOutput(), buffer);
        recipe.getItem().write(buffer);
    }
}

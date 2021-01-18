package xyz.brassgoggledcoders.transport.recipe.module;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModuleFinishedRecipe implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Module<?> module;
    private final Ingredient item;

    public ModuleFinishedRecipe(ResourceLocation id, Module<?> module, Ingredient item) {
        this.id = id;
        this.module = module;
        this.item = item;
    }


    @Override
    public void serialize(@Nonnull JsonObject json) {
        json.add("module", Module.toJson(module));
        json.add("item", item.serialize());
    }

    @Override
    @Nonnull
    public ResourceLocation getID() {
        return id;
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return TransportRecipes.MODULE_RECIPE_SERIALIZER.get();
    }

    @Nullable
    @Override
    public JsonObject getAdvancementJson() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementID() {
        return null;
    }
}

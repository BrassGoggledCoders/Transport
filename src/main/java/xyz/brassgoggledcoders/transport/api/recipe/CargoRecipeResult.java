package xyz.brassgoggledcoders.transport.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CargoRecipeResult implements IFinishedRecipe {
    private final Cargo cargo;
    private final IFinishedRecipe result;

    public CargoRecipeResult(Cargo cargo, IFinishedRecipe result) {
        this.cargo = cargo;
        this.result = result;
    }

    @Override
    public void serialize(@Nonnull JsonObject json) {
        result.serialize(json);
        json.getAsJsonObject("result").addProperty("cargo", String.valueOf(cargo.getRegistryName()));
    }

    @Override
    @Nonnull
    public ResourceLocation getID() {
        return result.getID();
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return TransportRecipes.CARGO_SERIALIZER.get();
    }

    @Nullable
    @Override
    public JsonObject getAdvancementJson() {
        return result.getAdvancementJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementID() {
        return result.getAdvancementID();
    }
}

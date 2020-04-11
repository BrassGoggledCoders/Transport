package xyz.brassgoggledcoders.transport.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CargoRecipeResult implements IFinishedRecipe {
    private final CargoModule cargoModule;
    private final IFinishedRecipe result;

    public CargoRecipeResult(CargoModule cargoModule, IFinishedRecipe result) {
        this.cargoModule = cargoModule;
        this.result = result;
    }

    @Override
    public void serialize(@Nonnull JsonObject json) {
        result.serialize(json);
        json.getAsJsonObject("result").addProperty("cargo", String.valueOf(cargoModule.getRegistryName()));
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

package xyz.brassgoggledcoders.transport.api.recipe;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;

import java.util.function.Consumer;

public class CargoShapelessRecipeBuilder extends ShapelessRecipeBuilder {
    private final CargoModule cargoModule;

    public CargoShapelessRecipeBuilder(IItemProvider result, CargoModule cargoModule, int count) {
        super(result, count);
        this.cargoModule = cargoModule;
    }

    public static CargoShapelessRecipeBuilder start(IItemProvider result, CargoModule cargoModule) {
        return start(result, cargoModule, 1);
    }

    public static CargoShapelessRecipeBuilder start(IItemProvider result, CargoModule cargoModule, int count) {
        return new CargoShapelessRecipeBuilder(result, cargoModule, count);
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        super.build(recipe -> consumer.accept(new CargoRecipeResult(cargoModule, recipe)), id);
    }
}

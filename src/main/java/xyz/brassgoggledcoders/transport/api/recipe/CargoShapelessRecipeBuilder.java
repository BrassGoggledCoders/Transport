package xyz.brassgoggledcoders.transport.api.recipe;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;

import java.util.function.Consumer;

public class CargoShapelessRecipeBuilder extends ShapelessRecipeBuilder {
    private final Cargo cargo;

    public CargoShapelessRecipeBuilder(IItemProvider result, Cargo cargo, int count) {
        super(result, count);
        this.cargo = cargo;
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        super.build(recipe -> consumer.accept(new CargoRecipeResult(cargo, recipe)), id);
    }

    public static CargoShapelessRecipeBuilder start(IItemProvider result, Cargo cargo) {
        return start(result, cargo, 1);
    }

    public static CargoShapelessRecipeBuilder start(IItemProvider result, Cargo cargo, int count) {
        return new CargoShapelessRecipeBuilder(result, cargo, count);
    }
}

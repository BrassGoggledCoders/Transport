package xyz.brassgoggledcoders.transport.recipe.module;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.module.Module;

import java.util.Objects;
import java.util.function.Consumer;

public class ModuleRecipeBuilder {
    private final Module<?> module;

    private Ingredient item;

    public ModuleRecipeBuilder(Module<?> module) {
        this.module = module;
    }

    public ModuleRecipeBuilder withItem(Ingredient item) {
        this.item = item;
        return this;
    }

    public void save(Consumer<IFinishedRecipe> recipeConsumer) {
        ResourceLocation id = Objects.requireNonNull(module.getRegistryName());
        save(recipeConsumer, new ResourceLocation(id.getNamespace(), "module/" + id.getPath()));
    }

    public void save(Consumer<IFinishedRecipe> recipeConsumer, ResourceLocation id) {
        recipeConsumer.accept(new ModuleFinishedRecipe(id, module, item));
    }

    public static ModuleRecipeBuilder create(Module<?> module) {
        return new ModuleRecipeBuilder(module);
    }

}

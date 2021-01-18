package xyz.brassgoggledcoders.transport.recipe.module;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ModuleRecipe implements IRecipe<ModuleVessel> {
    private final ResourceLocation id;
    private final Module<?> module;
    private final Ingredient item;

    public ModuleRecipe(ResourceLocation id, Module<?> module, Ingredient item) {
        this.id = id;
        this.module = module;
        this.item = item;

    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(ModuleVessel moduleVessel, World world) {
        return moduleVessel.matches(this);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ItemStack getCraftingResult(ModuleVessel moduleVessel) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public Ingredient getItem() {
        return item;
    }

    public Module<?> getModuleOutput() {
        return module;
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return TransportRecipes.MODULE_RECIPE_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return TransportRecipes.MODULE_RECIPE_TYPE;
    }
}

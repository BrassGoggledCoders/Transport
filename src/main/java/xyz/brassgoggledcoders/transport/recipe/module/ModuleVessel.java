package xyz.brassgoggledcoders.transport.recipe.module;

import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.transport.recipe.EmptyInventory;

public class ModuleVessel extends EmptyInventory {
    private final ItemStack inputStack;

    public ModuleVessel(ItemStack inputStack) {
        this.inputStack = inputStack;
    }

    public boolean matches(ModuleRecipe moduleRecipe) {
        return moduleRecipe.getItem().test(inputStack);
    }

    public static ModuleVessel of(ItemStack itemStack) {
        return new ModuleVessel(itemStack);
    }
}

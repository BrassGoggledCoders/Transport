package xyz.brassgoggledcoders.transport.compat.jei.module;

import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;

public class ModuleRecipe {
    private final ItemStack moduleItem;
    private final ItemStack modularInput;
    private final ItemStack modularOutput;

    private final ModuleSlot moduleSlot;

    public ModuleRecipe(ItemStack moduleItem, ItemStack modularInput, ItemStack modularOutput, ModuleSlot moduleSlot) {
        this.moduleItem = moduleItem;
        this.modularInput = modularInput;
        this.modularOutput = modularOutput;
        this.moduleSlot = moduleSlot;
    }

    public ItemStack getModuleItem() {
        return moduleItem;
    }

    public ItemStack getModularInput() {
        return modularInput;
    }

    public ItemStack getModularOutput() {
        return modularOutput;
    }

    public ModuleSlot getModuleSlot() {
        return moduleSlot;
    }
}

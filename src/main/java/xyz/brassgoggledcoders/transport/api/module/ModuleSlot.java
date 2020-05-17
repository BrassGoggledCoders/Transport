package xyz.brassgoggledcoders.transport.api.module;

import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;

import java.util.function.BiFunction;

public class ModuleSlot extends ForgeRegistryEntry<ModuleSlot> {
    private String translationKey = null;
    private ITextComponent displayName = null;
    private final BiFunction<IModularEntity, Module<?>, Boolean> isModuleValidFor;


    public ModuleSlot(BiFunction<IModularEntity, Module<?>, Boolean> isModuleValidFor) {
        this.isModuleValidFor = isModuleValidFor;
    }

    public String getTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("module_slot", this.getRegistryName());
        }
        return this.translationKey;
    }

    public ITextComponent getDisplayName() {
        if (this.displayName == null) {
            this.displayName = new TranslationTextComponent(this.getTranslationKey());
        }
        return this.displayName;
    }

    public boolean isModuleValid(IModularEntity entity, Module<?> module) {
        return this.isModuleValidFor.apply(entity, module);
    }
}

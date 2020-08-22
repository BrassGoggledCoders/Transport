package xyz.brassgoggledcoders.transport.api.module;

import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class ModuleSlot extends ForgeRegistryEntry<ModuleSlot> {
    private String translationKey = null;
    private ITextComponent displayName = null;
    private final BiPredicate<IModularEntity, Module<?>> isModuleValidFor;


    public ModuleSlot(BiPredicate<IModularEntity, Module<?>> isModuleValidFor) {
        this.isModuleValidFor = isModuleValidFor;
    }

    @Nonnull
    public String getTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("module_slot", this.getRegistryName());
        }
        return this.translationKey;
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        if (this.displayName == null) {
            this.displayName = new TranslationTextComponent(this.getTranslationKey());
        }
        return this.displayName;
    }

    public boolean isModuleValid(IModularEntity entity, Module<?> module) {
        return this.isModuleValidFor.test(entity, module);
    }
}

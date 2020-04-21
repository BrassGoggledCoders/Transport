package xyz.brassgoggledcoders.transport.api.module.slot;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;

import java.util.function.BiFunction;

public class ModuleSlot {
    private final String name;
    private final String translationKey;
    private final BiFunction<IModularEntity, Module<?>, Boolean> isModuleValidFor;

    private ITextComponent displayName;

    public ModuleSlot(String name, String translationKey, BiFunction<IModularEntity, Module<?>, Boolean> isModuleValidFor) {
        this.name = name;
        this.translationKey = translationKey;
        this.isModuleValidFor = isModuleValidFor;
    }

    public String getName() {
        return this.name;
    }

    public String getTranslationKey() {
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

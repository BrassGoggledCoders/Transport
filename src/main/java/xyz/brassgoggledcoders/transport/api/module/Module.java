package xyz.brassgoggledcoders.transport.api.module;

import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class Module<MOD extends Module<MOD>> extends ForgeRegistryEntry<MOD> implements IItemProvider {
    private final BiFunction<MOD, IModularEntity, ? extends ModuleInstance<MOD>> instanceCreator;
    private final Supplier<ModuleType<MOD>> componentType;
    private String translationKey;
    private ITextComponent name;

    public Module(Supplier<ModuleType<MOD>> componentType, BiFunction<MOD, IModularEntity, ? extends ModuleInstance<MOD>> instanceCreator) {
        this.componentType = componentType;
        this.instanceCreator = instanceCreator;
    }

    @Nonnull
    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey(componentType.get().getName(), this.getRegistryName());
        }
        return translationKey;
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        if (name == null) {
            name = new TranslationTextComponent(this.getTranslationKey());
        }
        return name;
    }

    public boolean isValidFor(IModularEntity carrier) {
        return true;
    }

    public ModuleType<MOD> getType() {
        return componentType.get();
    }


    @SuppressWarnings("unchecked")
    @Nonnull
    public ModuleInstance<MOD> createInstance(IModularEntity carrier) {
        return instanceCreator.apply((MOD) this, carrier);
    }
}

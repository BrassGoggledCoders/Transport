package xyz.brassgoggledcoders.transport.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;

import javax.annotation.Nonnull;

public class ModuleTypeBuilder<M extends ModuleType, P> extends AbstractBuilder<ModuleType, M, P, ModuleTypeBuilder<M, P>> {
    public final NonNullSupplier<M> moduleSlotSupplier;

    public ModuleTypeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                             NonNullSupplier<M> moduleSlotSupplier) {
        super(owner, parent, name, callback, ModuleType.class);
        this.moduleSlotSupplier = moduleSlotSupplier;
    }

    public ModuleTypeBuilder<M, P> lang(String name) {
        return this.lang(ModuleType::getTranslationKey, name);
    }

    @Override
    @Nonnull
    protected M createEntry() {
        return moduleSlotSupplier.get();
    }
}

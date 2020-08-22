package xyz.brassgoggledcoders.transport.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;

import javax.annotation.Nonnull;

public class ModuleSlotBuilder<M extends ModuleSlot, P> extends AbstractBuilder<ModuleSlot, M, P, ModuleSlotBuilder<M, P>> {
    public final NonNullSupplier<M> moduleSlotSupplier;

    public ModuleSlotBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                             NonNullSupplier<M> moduleSlotSupplier) {
        super(owner, parent, name, callback, ModuleSlot.class);
        this.moduleSlotSupplier = moduleSlotSupplier;
    }

    public ModuleSlotBuilder<M, P> lang(String name) {
        return this.lang(ModuleSlot::getTranslationKey, name);
    }

    @Override
    @Nonnull
    protected M createEntry() {
        return moduleSlotSupplier.get();
    }
}

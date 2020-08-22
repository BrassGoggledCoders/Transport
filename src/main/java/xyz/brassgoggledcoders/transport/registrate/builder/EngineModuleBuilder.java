package xyz.brassgoggledcoders.transport.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.item.Item;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.item.ModuleItem;

import javax.annotation.Nonnull;

public class EngineModuleBuilder<E extends EngineModule, I extends Item, P> extends AbstractBuilder<EngineModule, E, P,
        EngineModuleBuilder<E, I, P>> {
    public final NonNullSupplier<E> cargoCreator;

    public EngineModuleBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                               NonNullSupplier<E> cargoCreator) {
        super(owner, parent, name, callback, EngineModule.class);
        this.cargoCreator = cargoCreator;
    }

    public EngineModuleBuilder<E, I, P> lang(String name) {
        return this.lang(EngineModule::getTranslationKey, name);
    }

    public ItemBuilder<I, EngineModuleBuilder<E, I, P>> item(NonNullBiFunction<NonNullSupplier<E>, Item.Properties, I> itemCreator) {
        return this.getOwner().item(this, properties -> itemCreator.apply(this.get(), properties));
    }

    public ItemBuilder<ModuleItem<EngineModule>, EngineModuleBuilder<E, I, P>> item() {
        return this.getOwner().item(this, properties -> new ModuleItem<>(this::getEntry, properties));
    }

    public ItemBuilder<I, EngineModuleBuilder<E, I, P>> item(String name, NonNullBiFunction<NonNullSupplier<E>, Item.Properties, I> itemCreator) {
        return this.getOwner().item(this, name, properties -> itemCreator.apply(this.get(), properties));
    }

    public ItemBuilder<ModuleItem<EngineModule>, EngineModuleBuilder<E, I, P>> item(String name) {
        return this.getOwner().item(this, name, properties -> new ModuleItem<>(this::getEntry, properties));
    }

    @Override
    @Nonnull
    protected E createEntry() {
        return cargoCreator.get();
    }
}

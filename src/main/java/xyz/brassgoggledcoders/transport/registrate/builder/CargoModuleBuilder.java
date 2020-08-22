package xyz.brassgoggledcoders.transport.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;

import javax.annotation.Nonnull;

public class CargoModuleBuilder<C extends CargoModule, P> extends AbstractBuilder<CargoModule, C, P, CargoModuleBuilder<C, P>> {
    public final NonNullSupplier<C> cargoCreator;

    public CargoModuleBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                              NonNullSupplier<C> cargoCreator) {
        super(owner, parent, name, callback, CargoModule.class);
        this.cargoCreator = cargoCreator;
    }

    public CargoModuleBuilder<C, P> lang(String name) {
        return this.lang(CargoModule::getTranslationKey, name);
    }

    @Override
    @Nonnull
    protected C createEntry() {
        return cargoCreator.get();
    }
}

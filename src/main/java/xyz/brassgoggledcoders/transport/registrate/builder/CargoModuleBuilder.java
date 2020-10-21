package xyz.brassgoggledcoders.transport.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;

import javax.annotation.Nonnull;

public class CargoModuleBuilder<C extends CargoModule, P> extends ModuleBuilder<CargoModule, C, P, CargoModuleBuilder<C, P>> {
    public CargoModuleBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                              NonNullSupplier<C> cargoCreator) {
        super(owner, parent, name, callback, CargoModule.class, cargoCreator);
    }
}

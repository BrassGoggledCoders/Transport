package xyz.brassgoggledcoders.transport.api;

import xyz.brassgoggledcoders.transport.api.cargo.CargoRegistry;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRendererLoader;

import java.util.Objects;

public class TransportAPI {
    private static CargoRegistry cargoRegistry = new CargoRegistry();
    private static ICargoRendererLoader cargoRendererLoader;

    public static ICargoRendererLoader getCargoRendererLoader() {
        return Objects.requireNonNull(cargoRendererLoader, "Cargo Renderer Loader has not be set");
    }

    public static void setCargoRendererLoader(ICargoRendererLoader cargoRendererLoader) {
        TransportAPI.cargoRendererLoader = cargoRendererLoader;
    }

    public static CargoRegistry getCargoRegistry() {
        return cargoRegistry;
    }
}

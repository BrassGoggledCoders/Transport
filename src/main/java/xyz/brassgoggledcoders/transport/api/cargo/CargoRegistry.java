package xyz.brassgoggledcoders.transport.api.cargo;

import xyz.brassgoggledcoders.transport.api.registry.TransportRegistry;

public class CargoRegistry extends TransportRegistry<ICargo> {
    private final ICargo empty = new CargoEmpty();

    @Override
    public ICargo getEmpty() {
        return empty;
    }
}

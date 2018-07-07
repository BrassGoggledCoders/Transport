package xyz.brassgoggledcoders.transport.api.cargo.render;

import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

public interface ICargoRenderer {
    void render(ICargoInstance cargo, ICargoCarrier cargoCarrier, float partialTicks);
}

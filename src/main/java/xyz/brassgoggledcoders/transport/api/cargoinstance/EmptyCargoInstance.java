package xyz.brassgoggledcoders.transport.api.cargoinstance;

import xyz.brassgoggledcoders.transport.content.TransportCargoes;

public class EmptyCargoInstance extends CargoInstance {
    public EmptyCargoInstance() {
        super(TransportCargoes.EMPTY.get());
    }
}

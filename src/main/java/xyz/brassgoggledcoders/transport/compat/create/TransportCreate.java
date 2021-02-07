package xyz.brassgoggledcoders.transport.compat.create;

import xyz.brassgoggledcoders.transport.api.TransportAPI;

public class TransportCreate {
    public TransportCreate() {
        TransportAPI.setConnectionChecker(new CreateConnectionChecker());
    }
}

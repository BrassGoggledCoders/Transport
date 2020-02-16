package xyz.brassgoggledcoders.transport.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;

public class TransportAPI {
    @CapabilityInject(ICargoCarrier.class)
    public static Capability<ICargoCarrier> CARRIER_CAP;
}

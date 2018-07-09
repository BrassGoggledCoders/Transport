package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.cargo.ICargo;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import java.util.Optional;

public interface ICargoCarrier {
    Optional<World> getWorld();

    ICargo getCargo();

    ICargoInstance getCargoInstance();
}

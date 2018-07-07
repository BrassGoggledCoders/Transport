package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.cargo.ICargo;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import java.lang.ref.WeakReference;
import java.util.Optional;

public class CargoCarrierEntity implements ICargoCarrier {
    private WeakReference<Entity> entity;

    public CargoCarrierEntity(Entity entity, ICargo cargo) {

    }

    @Override
    public Optional<World> getWorld() {
        return Optional.empty();
    }

    @Override
    public ICargo getCargo() {
        return null;
    }

    @Override
    public ICargoInstance getCargoInstance() {
        return Optional.empty();
    }

    @Override
    public float getBrightness() {
        return 0;
    }
}

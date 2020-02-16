package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargo.instance.CargoInstanceEmpty;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import java.util.Optional;

public class CargoCarrierEmpty implements ICargoCarrier {
    private CargoInstanceEmpty cargoInstanceEmpty = new CargoInstanceEmpty();
    @Override
    public Optional<World> getWorld() {
        return Optional.empty();
    }

    @Override
    public Cargo getCargo() {
        return null;
    }

    @Override
    public ICargoInstance getCargoInstance() {
        return cargoInstanceEmpty;
    }

    @Override
    public boolean canInteractWith(PlayerEntity entityPlayer) {
        return false;
    }
}

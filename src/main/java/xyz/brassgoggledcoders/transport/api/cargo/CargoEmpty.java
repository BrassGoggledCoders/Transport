package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.cargo.instance.CargoInstanceEmpty;

public class CargoEmpty extends Cargo {
    @Override
    public CargoInstanceEmpty create(World world) {
        return new CargoInstanceEmpty();
    }
}

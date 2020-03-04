package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.world.World;

public class EmptyCargo extends Cargo {
    public EmptyCargo() {
        super(blockSupplier);
    }

    @Override
    public CargoInstanceEmpty create(World world) {
        return new CargoInstanceEmpty();
    }
}

package xyz.brassgoggledcoders.transport.cargo;

import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nullable;

public class ItemCargo extends Cargo {
    public ItemCargo() {
        super(TransportBlocks.ITEM_LOADER::getBlock);
    }

    @Override
    public CargoInstance create(@Nullable World world) {
        return null;
    }
}

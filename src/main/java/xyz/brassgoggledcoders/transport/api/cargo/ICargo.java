package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;
import xyz.brassgoggledcoders.transport.api.registry.ITransportRegistryItem;

import javax.annotation.Nullable;

public interface ICargo extends ITransportRegistryItem {
    ICargoInstance create(@Nullable World world);
}

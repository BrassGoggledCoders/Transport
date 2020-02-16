package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import javax.annotation.Nullable;

public abstract class Cargo extends ForgeRegistryEntry<Cargo> {
    public abstract ICargoInstance create(@Nullable World world);
}

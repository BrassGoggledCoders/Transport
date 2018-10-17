package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.cargo.instance.CargoInstanceEmpty;

import javax.annotation.Nonnull;

public class CargoEmpty implements ICargo {
    public static final ResourceLocation NAME = new ResourceLocation("transport", "empty");

    @Override
    public CargoInstanceEmpty create(World world) {
        return new CargoInstanceEmpty();
    }

    @Nonnull
    @Override
    public ResourceLocation getRegistryName() {
        return NAME;
    }
}

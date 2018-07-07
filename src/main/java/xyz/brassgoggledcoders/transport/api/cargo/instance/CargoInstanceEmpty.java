package xyz.brassgoggledcoders.transport.api.cargo.instance;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import xyz.brassgoggledcoders.transport.api.cargo.render.EmptyCargoRenderer;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CargoInstanceEmpty implements ICargoInstance {
    private final ICargoRenderer renderer = new EmptyCargoRenderer();

    @Nonnull
    @Override
    public ICargoRenderer getCargoRenderer() {
        return renderer;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }
}

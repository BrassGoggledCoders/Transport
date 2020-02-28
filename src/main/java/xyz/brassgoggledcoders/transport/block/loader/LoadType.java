package xyz.brassgoggledcoders.transport.block.loader;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum LoadType implements IStringSerializable {
    INPUT,
    OUTPUT,
    NONE;

    @Override
    @Nonnull
    public String getName() {
        return this.name().toLowerCase();
    }
}

package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CargoInstance extends ModuleInstance<Cargo> {

    public CargoInstance(Cargo cargo, IModularEntity modularEntity) {
        super(cargo, modularEntity);
    }

    public BlockState getBlockState() {
        return this.getModule().getDefaultBlockState();
    }

    public int getComparatorLevel() {
        return -1;
    }
}

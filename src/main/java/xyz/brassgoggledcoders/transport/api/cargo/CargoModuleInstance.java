package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.block.BlockState;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

public class CargoModuleInstance extends ModuleInstance<CargoModule> {

    public CargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity) {
        super(cargoModule, modularEntity);
    }

    public BlockState getBlockState() {
        return this.getModule().getDefaultBlockState();
    }

    public int getComparatorLevel() {
        return -1;
    }
}

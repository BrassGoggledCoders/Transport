package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

import javax.annotation.Nullable;

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

    @Nullable
    public TileEntity asTileEntity() {
        if (this.getBlockState().hasTileEntity()) {
            return this.getBlockState().createTileEntity(this.getWorld());
        } else {
            return null;
        }
    }

    public void fromTileEntity(TileEntity tileEntity) {

    }
}

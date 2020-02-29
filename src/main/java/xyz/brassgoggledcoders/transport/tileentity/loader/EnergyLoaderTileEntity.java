package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.List;

public class EnergyLoaderTileEntity extends BasicLoaderTileEntity<IEnergyStorage> {
    public EnergyLoaderTileEntity() {
        super(TransportBlocks.ENERGY_LOADER.getTileEntityType(), CapabilityEnergy.ENERGY);
    }

    @Override
    protected void transfer(IEnergyStorage from, IEnergyStorage to) {

    }

    @Override
    protected LazyOptional<IEnergyStorage> getInternalCAP() {
        return null;
    }

    @Override
    protected LazyOptional<IEnergyStorage> createOutputCAP() {
        return null;
    }

    @Override
    protected LazyOptional<IEnergyStorage> createInputCAP() {
        return null;
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return null;
    }
}

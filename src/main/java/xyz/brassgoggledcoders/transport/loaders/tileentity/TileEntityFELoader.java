package xyz.brassgoggledcoders.transport.loaders.tileentity;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.transport.library.tileentity.loader.TileEntityLoaderBase;

public class TileEntityFELoader extends TileEntityLoaderBase<IEnergyStorage> {
    @Override
    public Capability<IEnergyStorage> getCapabilityType() {
        return CapabilityEnergy.ENERGY;
    }

    @Override
    public <T> T castCapability(IEnergyStorage iEnergyStorage) {
        return CapabilityEnergy.ENERGY.cast(iEnergyStorage);
    }

    @Override
    public IEnergyStorage getInternalCapability() {
        return null;
    }

    @Override
    public IEnergyStorage getOutputCapability() {
        return null;
    }

    @Override
    public IEnergyStorage getInputCapability() {
        return null;
    }

    @Override
    protected boolean transfer(IEnergyStorage from, IEnergyStorage to) {
        return false;
    }
}

package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.teamacronymcoders.base.capability.energy.EnergyStorageDirectional;
import com.teamacronymcoders.base.capability.energy.EnergyStorageSerializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityFELoader extends TileEntityLoaderBase<IEnergyStorage> {
    private final EnergyStorageSerializable energyStorage;
    private final EnergyStorageDirectional input;
    private final EnergyStorageDirectional output;

    public TileEntityFELoader() {
        energyStorage = new EnergyStorageSerializable(100000, 10000, 10000);
        input = new EnergyStorageDirectional(energyStorage, true);
        output = new EnergyStorageDirectional(energyStorage, false);
    }

    @Override
    protected void readCapability(NBTTagCompound data) {
        energyStorage.deserializeNBT(data.getCompoundTag("energy"));
    }

    @Override
    protected void writeCapability(NBTTagCompound data) {
        data.setTag("energy", energyStorage.serializeNBT());
    }

    @Override
    public Capability<IEnergyStorage> getCapabilityType() {
        return CapabilityEnergy.ENERGY;
    }

    @Override
    public IEnergyStorage getInternalCapability() {
        return energyStorage;
    }

    @Override
    public IEnergyStorage getOutputCapability() {
        return output;
    }

    @Override
    public IEnergyStorage getInputCapability() {
        return input;
    }

    @Override
    protected boolean transfer(IEnergyStorage from, IEnergyStorage to) {
        int amountSimPulled = from.extractEnergy(10000, true);
        if (amountSimPulled > 0) {
            int amountSimPushed = to.receiveEnergy(amountSimPulled, true);
            if (amountSimPushed > 0) {
                return to.receiveEnergy(from.extractEnergy(amountSimPushed, false), false) > 0;
            }
        }
        return false;
    }
}

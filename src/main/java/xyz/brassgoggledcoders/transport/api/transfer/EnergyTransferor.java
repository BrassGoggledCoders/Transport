package xyz.brassgoggledcoders.transport.api.transfer;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyTransferor implements ITransferor<IEnergyStorage> {
    @Override
    public Capability<IEnergyStorage> getCapability() {
        return CapabilityEnergy.ENERGY;
    }

    @Override
    public boolean transfer(IEnergyStorage from, IEnergyStorage to, int transferAmount) {
        int amountSimPulled = from.extractEnergy(transferAmount, true);
        if (amountSimPulled > 0) {
            int amountSimPushed = to.receiveEnergy(amountSimPulled, true);
            if (amountSimPushed > 0) {
                return to.receiveEnergy(from.extractEnergy(amountSimPushed, false), false) > 0;
            }
        }
        return false;
    }

    @Override
    public int getDefaultAmount() {
        return 5000;
    }
}

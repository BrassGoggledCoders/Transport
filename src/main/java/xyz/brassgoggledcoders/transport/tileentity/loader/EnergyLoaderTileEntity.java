package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.transport.capability.EnergyComponent;
import xyz.brassgoggledcoders.transport.capability.EnergyStorageDirectional;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.Collections;
import java.util.List;

public class EnergyLoaderTileEntity extends BasicLoaderTileEntity<IEnergyStorage> {
    private final EnergyComponent energyComponent;
    private final LazyOptional<IEnergyStorage> lazyEnergy;

    public EnergyLoaderTileEntity() {
        super(TransportBlocks.ENERGY_LOADER.getTileEntityType(), CapabilityEnergy.ENERGY);
        this.energyComponent = new EnergyComponent(10000, 79, 24);
        this.lazyEnergy = LazyOptional.of(() -> energyComponent);

    }

    @Override
    protected void transfer(IEnergyStorage from, IEnergyStorage to) {
        int amountSimPulled = from.extractEnergy(10000, true);
        if (amountSimPulled > 0) {
            int amountSimPushed = to.receiveEnergy(amountSimPulled, true);
            if (amountSimPushed > 0) {
                to.receiveEnergy(from.extractEnergy(amountSimPushed, false), false);
            }
        }
    }

    @Override
    protected LazyOptional<IEnergyStorage> getInternalCAP() {
        return lazyEnergy;
    }

    @Override
    protected LazyOptional<IEnergyStorage> createOutputCAP() {
        return LazyOptional.of(() -> new EnergyStorageDirectional(energyComponent, false));
    }

    @Override
    protected LazyOptional<IEnergyStorage> createInputCAP() {
        return LazyOptional.of(() -> new EnergyStorageDirectional(energyComponent, true));
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.energyComponent.deserializeNBT(nbt.getCompound("energy"));
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = super.serializeNBT();
        compoundNBT.put("energy", energyComponent.serializeNBT());
        return compoundNBT;
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return energyComponent.getScreenAddons();
    }
}

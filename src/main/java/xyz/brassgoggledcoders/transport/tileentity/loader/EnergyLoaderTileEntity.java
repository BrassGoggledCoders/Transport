package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.transport.capability.EnergyStorageDirectional;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.List;

public class EnergyLoaderTileEntity extends BasicLoaderTileEntity<IEnergyStorage> {
    private final EnergyStorageComponent energyComponent;
    private final LazyOptional<IEnergyStorage> lazyEnergy;

    public EnergyLoaderTileEntity() {
        super(TransportBlocks.ENERGY_LOADER.getTileEntityType(), CapabilityEnergy.ENERGY);
        this.energyComponent = new EnergyStorageComponent(10000, 79, 24);
        this.lazyEnergy = LazyOptional.of(() -> energyComponent);

    }

    @Override
    protected void transfer(IEnergyStorage from, IEnergyStorage to) {
        int amountSimPulled = from.extractEnergy(5000, true);
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
    protected CompoundNBT serializeCap() {
        return energyComponent.serializeNBT();
    }

    @Override
    protected void deserializeCap(CompoundNBT compoundNBT) {
        energyComponent.deserializeNBT(compoundNBT);
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return energyComponent.getScreenAddons();
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return energyComponent.getContainerAddons();
    }
}

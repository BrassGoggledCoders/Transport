package xyz.brassgoggledcoders.transport.cargoinstance.capability;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;

import java.util.Collections;
import java.util.List;

public class EnergyCargoInstance extends CapabilityCargoInstance<IEnergyStorage> {
    private final EnergyStorageComponent energy;
    private final LazyOptional<IEnergyStorage> lazyEnergy;

    public EnergyCargoInstance(Cargo cargo, IModularEntity modularEntity) {
        super(cargo, modularEntity, CapabilityEnergy.ENERGY);
        this.energy = new EnergyStorageComponent(10000, 79, 24);
        this.lazyEnergy = LazyOptional.of(() -> energy);
    }

    @Override
    protected LazyOptional<IEnergyStorage> getLazyOptional() {
        return lazyEnergy;
    }

    @Override
    protected CompoundNBT serializeCapability() {
        return energy.serializeNBT();
    }

    @Override
    protected void deserializeCapability(CompoundNBT nbt) {
        energy.deserializeNBT(nbt);
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return energy.getScreenAddons();
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return energy.getContainerAddons();
    }
}

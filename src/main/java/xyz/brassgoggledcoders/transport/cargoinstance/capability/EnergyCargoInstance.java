package xyz.brassgoggledcoders.transport.cargoinstance.capability;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.capability.EnergyComponent;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddon;

import java.util.Collections;
import java.util.List;

public class EnergyCargoInstance extends CapabilityCargoInstance<IEnergyStorage> {
    private final EnergyComponent energy;
    private final LazyOptional<IEnergyStorage> lazyEnergy;

    public EnergyCargoInstance(Cargo cargo) {
        super(cargo, CapabilityEnergy.ENERGY);
        this.energy = new EnergyComponent(10000, 79, 24);
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
    public List<IContainerAddon> getContainerAddons() {
        return Collections.singletonList(energy);
    }
}

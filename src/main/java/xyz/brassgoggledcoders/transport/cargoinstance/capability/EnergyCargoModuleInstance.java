package xyz.brassgoggledcoders.transport.cargoinstance.capability;

import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.entity.EntityWorldPosCallable;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.capability.energy.BasicEnergyStorage;
import xyz.brassgoggledcoders.transport.container.loader.EnergyLoaderContainer;
import xyz.brassgoggledcoders.transport.content.TransportContainers;

import javax.annotation.Nullable;

public class EnergyCargoModuleInstance extends CapabilityCargoModuleInstance<IEnergyStorage> {
    private final BasicEnergyStorage energy;
    private final LazyOptional<IEnergyStorage> lazyEnergy;

    public EnergyCargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity) {
        this(cargoModule, modularEntity, 10000);
    }

    public EnergyCargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity, int energyAmount) {
        super(cargoModule, modularEntity, CapabilityEnergy.ENERGY);
        this.energy = new BasicEnergyStorage(energyAmount);
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
    public int getComparatorLevel() {
        return energy.getComparatorLevel();
    }

    @Nullable
    @Override
    public Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> getContainerCreator() {
        return (id, playerInventory, playerEntity) -> new EnergyLoaderContainer(
                TransportContainers.ENERGY_LOADER.get(),
                id,
                playerInventory,
                new EntityWorldPosCallable(this.getModularEntity()),
                this.energy::getEnergyStored,
                this.energy.getMaxEnergyStored()
        );
    }
}

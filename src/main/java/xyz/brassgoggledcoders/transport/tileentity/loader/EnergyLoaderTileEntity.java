package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import xyz.brassgoggledcoders.transport.capability.EnergyStorageDirectional;
import xyz.brassgoggledcoders.transport.util.TransferUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class EnergyLoaderTileEntity extends BasicLoaderTileEntity<IEnergyStorage> {
    private final EnergyStorageComponent<?> energyComponent;
    private final LazyOptional<IEnergyStorage> lazyEnergy;

    public EnergyLoaderTileEntity(TileEntityType<? extends EnergyLoaderTileEntity> tileEntityType) {
        super(tileEntityType, CapabilityEnergy.ENERGY, TransferUtils::transferEnergy);
        this.energyComponent = new EnergyStorageComponent<>(10000, 79, 24);
        this.lazyEnergy = LazyOptional.of(() -> energyComponent);

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
    @Nonnull
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return energyComponent.getScreenAddons();
    }

    @Override
    @Nonnull
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return energyComponent.getContainerAddons();
    }
}

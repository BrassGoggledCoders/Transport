package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.transport.api.cargo.instance.CargoInstanceCap;

import javax.annotation.Nonnull;

public class CargoBasic<CAP extends INBTSerializable<CompoundNBT>> extends Cargo {
    private final ResourceLocation registryLocation;
    private final Capability<CAP> capabilityType;
    private final CAP capabilityInstance;

    public CargoBasic(ResourceLocation registryLocation, Capability<CAP> capabilityType, CAP capabilityInstance) {
        this.registryLocation = registryLocation;
        this.capabilityType = capabilityType;
        this.capabilityInstance = capabilityInstance;
    }

    @Override
    public CargoInstanceCap<CAP> create(World world) {
        return new CargoInstanceCap<>(registryLocation, capabilityType, capabilityInstance);
    }
}

package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import xyz.brassgoggledcoders.transport.api.cargo.instance.CargoInstanceCap;

import javax.annotation.Nonnull;

public class CargoBasic<CAP> implements ICargo<CargoInstanceCap<CAP>> {
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
        return new CargoInstanceCap<>(capabilityType, capabilityInstance, registryLocation);
    }

    @Nonnull
    @Override
    public ResourceLocation getRegistryName() {
        return registryLocation;
    }
}

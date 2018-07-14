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
    private final String localizationKey;

    public CargoBasic(ResourceLocation registryLocation, Capability<CAP> capabilityType, CAP capabilityInstance) {
        this.registryLocation = registryLocation;
        this.capabilityType = capabilityType;
        this.capabilityInstance = capabilityInstance;
        this.localizationKey = "cargo." + registryLocation.toString().replace(":", ".");
    }

    @Override
    public CargoInstanceCap<CAP> create(World world) {
        return new CargoInstanceCap<>(localizationKey, capabilityType, capabilityInstance, registryLocation);
    }

    @Nonnull
    @Override
    public ResourceLocation getRegistryName() {
        return registryLocation;
    }
}

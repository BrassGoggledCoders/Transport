package xyz.brassgoggledcoders.transport.api.cargo.instance;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CargoInstanceCap<CAP> implements ICargoInstance {
    private final ICargoRenderer cargoRenderer;
    private final Capability<CAP> capabilityType;
    private final CAP capabilityInstance;

    public CargoInstanceCap(Capability<CAP> capabilityType, CAP capabilityInstance, ResourceLocation cargoBlock) {
        this(capabilityType, capabilityInstance, "xyz.brassgoggledcoders.transport.library.render.cargo.CargoBlockRenderer",
                Optional.ofNullable(ForgeRegistries.BLOCKS.getValue(cargoBlock))
                    .map(Block::getDefaultState)
                    .orElse(Blocks.AIR.getDefaultState()));
    }

    public CargoInstanceCap(Capability<CAP> capabilityType, CAP capabilityInstance, String cargoRenderer, Object... inputs) {
        this.cargoRenderer = TransportAPI.getCargoRendererLoader().loadRenderer(cargoRenderer, inputs);
        this.capabilityType = capabilityType;
        this.capabilityInstance = capabilityInstance;
    }

    @Nonnull
    @Override
    public ICargoRenderer getCargoRenderer() {
        return cargoRenderer;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == this.capabilityType;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? this.capabilityType.cast(this.capabilityInstance) : null;
    }
}

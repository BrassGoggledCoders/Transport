package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.TransportAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderCargoCarrierItem implements ICapabilityProvider {
    private final CargoCarrierItem cargoCarrierItem;
    private final LazyOptional<ICargoCarrier> lazyOptional;

    public CapabilityProviderCargoCarrierItem(ItemStack stack) {
        cargoCarrierItem = new CargoCarrierItem(stack);
        lazyOptional = LazyOptional.of(() -> cargoCarrierItem);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == TransportAPI.CARRIER_CAP ? lazyOptional.cast() : LazyOptional.empty();
    }
}

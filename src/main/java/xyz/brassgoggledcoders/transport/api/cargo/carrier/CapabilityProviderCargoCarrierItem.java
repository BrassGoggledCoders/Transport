package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.transport.api.cargo.CapabilityCargo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderCargoCarrierItem implements ICapabilityProvider {
    public CapabilityProviderCargoCarrierItem(ItemStack stack) {
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityCargo.CARRIER;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }
}

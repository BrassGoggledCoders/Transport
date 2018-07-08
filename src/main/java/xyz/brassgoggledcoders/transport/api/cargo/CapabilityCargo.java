package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.CargoCarrierEmpty;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;

import javax.annotation.Nullable;

public class CapabilityCargo {
    @CapabilityInject(ICargoCarrier.class)
    public static Capability<ICargoCarrier> CARRIER;

    public static void register() {
        CapabilityManager.INSTANCE.register(ICargoCarrier.class, new Capability.IStorage<ICargoCarrier>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<ICargoCarrier> capability, ICargoCarrier instance, EnumFacing side) {
                return null;
            }

            @Override
            public void readNBT(Capability<ICargoCarrier> capability, ICargoCarrier instance, EnumFacing side, NBTBase nbt) {

            }
        }, CargoCarrierEmpty::new);
    }
}

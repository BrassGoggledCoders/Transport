package xyz.brassgoggledcoders.transport.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;

public class TransportAPI {
    @CapabilityInject(ICargoCarrier.class)
    public static Capability<ICargoCarrier> CARRIER_CAP;

    public static IForgeRegistry<Cargo> CARGO = new RegistryBuilder<Cargo>()
            .setName(new ResourceLocation("transport", "cargo"))
            .setType(Cargo.class)
            .create();

    public static ResourceLocation EMPTY_CARGO_RL = new ResourceLocation("transport", "empty");
    public static RegistryObject<Cargo> EMPTY_CARGO = RegistryObject.of(EMPTY_CARGO_RL, CARGO);
}

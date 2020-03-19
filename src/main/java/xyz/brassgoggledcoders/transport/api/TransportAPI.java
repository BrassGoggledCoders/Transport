package xyz.brassgoggledcoders.transport.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;

public class TransportAPI {
    public static final ResourceLocation EMPTY_CARGO_RL = new ResourceLocation("transport", "empty");
    public static ForgeRegistry<Cargo> CARGO = (ForgeRegistry<Cargo>) new RegistryBuilder<Cargo>()
            .setName(new ResourceLocation("transport", "cargo"))
            .setType(Cargo.class)
            .create();
    public static final RegistryObject<Cargo> EMPTY_CARGO = RegistryObject.of(EMPTY_CARGO_RL, CARGO);
}

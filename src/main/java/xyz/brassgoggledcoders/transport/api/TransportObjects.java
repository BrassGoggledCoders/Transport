package xyz.brassgoggledcoders.transport.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.api.engine.Engine;

public class TransportObjects {
    public static final RegistryObject<ModuleType<Engine>> ENGINE_TYPE = RegistryObject.of(
            new ResourceLocation("transport", "engine"), TransportAPI.MODULE_TYPE.get());

    public static final RegistryObject<ModuleType<Cargo>> CARGO_TYPE = RegistryObject.of(
            new ResourceLocation("transport", "cargo"), TransportAPI.MODULE_TYPE.get());
}
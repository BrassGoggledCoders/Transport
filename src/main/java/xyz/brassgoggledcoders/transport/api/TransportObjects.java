package xyz.brassgoggledcoders.transport.api;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;

public class TransportObjects {
    public static final RegistryObject<ModuleType> ENGINE_TYPE = RegistryObject.of(
            new ResourceLocation("transport", "engine"),
            TransportAPI.MODULE_TYPE.get()
    );

    public static final RegistryObject<ModuleType> CARGO_TYPE = RegistryObject.of(
            new ResourceLocation("transport", "cargo"),
            TransportAPI.MODULE_TYPE.get()
    );

    public static final RegistryObject<ContainerType<?>> VEHICLE = RegistryObject.of(
            new ResourceLocation("transport", "module"),
            ForgeRegistries.CONTAINERS
    );
}

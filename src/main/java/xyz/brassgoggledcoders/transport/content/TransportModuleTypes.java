package xyz.brassgoggledcoders.transport.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.engine.Engine;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;

@SuppressWarnings("unused")
public class TransportModuleTypes {
    private static final DeferredRegister<ModuleType<?>> COMPONENTS = new DeferredRegister<>(
            TransportAPI.MODULE_TYPE.get(), Transport.ID);

    public static final RegistryObject<ModuleType<Engine>> ENGINE = COMPONENTS.register("engine",
            () -> new ModuleType<>(TransportAPI::getEngine));

    public static final RegistryObject<ModuleType<Cargo>> CARGO = COMPONENTS.register("cargo",
            () -> new ModuleType<>(TransportAPI::getCargo));

    public static void register(IEventBus modBus) {
        COMPONENTS.register(modBus);
    }
}

package xyz.brassgoggledcoders.transport.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;

@SuppressWarnings("unused")
public class TransportModuleTypes {
    private static final DeferredRegister<ModuleType<?>> COMPONENTS = DeferredRegister.create(
            TransportAPI.MODULE_TYPE.get(), Transport.ID);

    public static final RegistryObject<ModuleType<EngineModule>> ENGINE = COMPONENTS.register("engine",
            () -> new ModuleType<>(TransportAPI::getEngine, TransportAPI::getEngines));

    public static final RegistryObject<ModuleType<CargoModule>> CARGO = COMPONENTS.register("cargo",
            () -> new ModuleType<>(TransportAPI::getCargo, TransportAPI::getCargo));

    public static void register(IEventBus modBus) {
        COMPONENTS.register(modBus);
    }
}

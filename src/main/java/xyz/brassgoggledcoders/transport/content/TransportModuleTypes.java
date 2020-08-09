package xyz.brassgoggledcoders.transport.content;

import com.google.common.collect.Lists;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;

@SuppressWarnings("unused")
public class TransportModuleTypes {
    private static final DeferredRegister<ModuleType> COMPONENTS = DeferredRegister.create(ModuleType.class, Transport.ID);

    public static final RegistryObject<ModuleType> ENGINE = COMPONENTS.register("engine",
            () -> new ModuleType(TransportAPI::getEngine, () -> Lists.newArrayList(TransportAPI.getEngines())));

    public static final RegistryObject<ModuleType> CARGO = COMPONENTS.register("cargo",
            () -> new ModuleType(TransportAPI::getCargo,  () -> Lists.newArrayList(TransportAPI.getCargo())));

    public static void register(IEventBus modBus) {
        COMPONENTS.register(modBus);
    }
}

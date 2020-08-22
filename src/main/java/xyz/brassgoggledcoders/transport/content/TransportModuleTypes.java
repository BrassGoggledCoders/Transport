package xyz.brassgoggledcoders.transport.content;

import com.google.common.collect.Lists;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;

@SuppressWarnings("unused")
public class TransportModuleTypes {
    public static final RegistryEntry<ModuleType> ENGINE = Transport.getRegistrate()
            .object("engine")
            .moduleType(TransportAPI::getEngine, () -> Lists.newArrayList(TransportAPI.getEngines()))
            .lang("Engine")
            .register();

    public static final RegistryEntry<ModuleType> CARGO = Transport.getRegistrate()
            .object("cargo")
            .moduleType(TransportAPI::getCargo, () -> Lists.newArrayList(TransportAPI.getCargo()))
            .lang("Cargo")
            .register();

    public static void setup() {
    }
}

package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;

public class TransportModuleSlots {
    public static final RegistryEntry<ModuleSlot> NONE = Transport.getRegistrate()
            .object("none")
            .moduleSlot((modularEntity, module) -> false)
            .lang("Not a")
            .register();

    public static final RegistryEntry<ModuleSlot> CARGO = Transport.getRegistrate()
            .object("cargo")
            .moduleSlot((modularEntity, module) -> module.getType() == TransportObjects.CARGO_TYPE.get())
            .lang("Cargo")
            .register();

    public static final RegistryEntry<ModuleSlot> BACK = Transport.getRegistrate()
            .object("back")
            .moduleSlot((modularEntity, module) -> module.getType() != TransportObjects.CARGO_TYPE.get())
            .lang("Back")
            .register();

    public static void setup() {

    }
}

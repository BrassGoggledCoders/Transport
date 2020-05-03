package xyz.brassgoggledcoders.transport.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;

public class TransportModuleSlots {
    private static final DeferredRegister<ModuleSlot> MODULE_SLOTS = new DeferredRegister<>(TransportAPI.MODULE_SLOT.get(),
            Transport.ID);

    public static final RegistryObject<ModuleSlot> NONE = MODULE_SLOTS.register("none", () -> new ModuleSlot(
            ((modularEntity, module) -> false)));

    public static final RegistryObject<ModuleSlot> CARGO = MODULE_SLOTS.register("cargo", () -> new ModuleSlot(
            ((modularEntity, module) -> module.getType() == TransportObjects.CARGO_TYPE.get())));

    public static final RegistryObject<ModuleSlot> BACK = MODULE_SLOTS.register("back", () -> new ModuleSlot(
            ((modularEntity, module) -> module.getType() != TransportObjects.CARGO_TYPE.get())));

    public static void register(IEventBus modBus) {
        MODULE_SLOTS.register(modBus);
    }
}

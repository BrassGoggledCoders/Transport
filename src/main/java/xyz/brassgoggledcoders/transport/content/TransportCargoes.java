package xyz.brassgoggledcoders.transport.content;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargo.EmptyCargo;
import xyz.brassgoggledcoders.transport.cargo.ItemCargo;

public class TransportCargoes {
    private static final DeferredRegister<Cargo> CARGO = new DeferredRegister<>(TransportAPI.CARGO, Transport.ID);

    public static final RegistryObject<EmptyCargo> EMPTY = CARGO.register("empty", EmptyCargo::new);
    public static final RegistryObject<ItemCargo> ITEM = CARGO.register("item", ItemCargo::new);

    public static void register(IEventBus eventBus) {
        CARGO.register(eventBus);
    }
}

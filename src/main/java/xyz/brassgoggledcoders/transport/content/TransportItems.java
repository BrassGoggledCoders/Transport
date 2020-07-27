package xyz.brassgoggledcoders.transport.content;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.item.RailBreakerItem;

@SuppressWarnings("unused")
public class TransportItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Transport.ID);

    public static final RegistryObject<RailBreakerItem> RAIL_BREAKER = ITEMS.register("rail_breaker",
            RailBreakerItem::new);

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}

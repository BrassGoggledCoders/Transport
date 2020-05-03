package xyz.brassgoggledcoders.transport.content;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.CargoCarrierMinecartEntity;
import xyz.brassgoggledcoders.transport.item.CargoCarrierMinecartItem;

public class TransportEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Transport.ID);

    public static RegistryObject<EntityType<CargoCarrierMinecartEntity>> CARGO_MINECART = ENTITIES.register("cargo_minecart",
            () -> EntityType.Builder.<CargoCarrierMinecartEntity>create(CargoCarrierMinecartEntity::new, EntityClassification.MISC)
                    .size(0.98F, 0.7F)
                    .build("cargo_minecart"));

    public static RegistryObject<CargoCarrierMinecartItem> CARGO_MINECART_ITEM = ITEMS.register("cargo_minecart",
            CargoCarrierMinecartItem::new);

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
        ITEMS.register(modBus);
    }
}

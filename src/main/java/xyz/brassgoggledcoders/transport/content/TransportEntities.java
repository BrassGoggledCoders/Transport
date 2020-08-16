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
import xyz.brassgoggledcoders.transport.entity.HulledBoatEntity;
import xyz.brassgoggledcoders.transport.entity.ModularBoatEntity;
import xyz.brassgoggledcoders.transport.item.CargoCarrierMinecartItem;
import xyz.brassgoggledcoders.transport.item.ModularBoatItem;

public class TransportEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Transport.ID);

    public static final EntityType<CargoCarrierMinecartEntity> CARGO_MINECART = Transport.getRegistrate()
            .object("cargo_minecart")
            .entity(CargoCarrierMinecartEntity::new, EntityClassification.MISC)
            .lang("Modular Minecart")
            .properties(properties -> properties.func_233606_a_(8).size(0.98F, 0.7F))
            .register();

    public static RegistryObject<EntityType<ModularBoatEntity>> MODULAR_BOAT = ENTITIES.register("modular_boat",
            () -> EntityType.Builder.<ModularBoatEntity>create(ModularBoatEntity::new, EntityClassification.MISC)
                    .size(1.375F, 0.5625F)
                    .func_233606_a_(10)
                    .build("modular_boat"));

    public static RegistryObject<CargoCarrierMinecartItem> CARGO_MINECART_ITEM = ITEMS.register("cargo_minecart",
            CargoCarrierMinecartItem::new);

    public static RegistryObject<ModularBoatItem> MODULAR_BOAT_ITEM = ITEMS.register("modular_boat",
            () -> new ModularBoatItem(new Item.Properties()
                    .group(Transport.ITEM_GROUP))
    );

    public static RegistryObject<EntityType<HulledBoatEntity>> HULLED_BOAT = ENTITIES.register("hulled_boat",
            () -> EntityType.Builder.<HulledBoatEntity>create(HulledBoatEntity::new, EntityClassification.MISC)
                    .size(1.375F, 0.5625F)
                    .func_233606_a_(10)
                    .build("hulled_boat"));

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
        ITEMS.register(modBus);
    }
}

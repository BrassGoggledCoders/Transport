package xyz.brassgoggledcoders.transport.content;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.CargoCarrierMinecartEntity;
import xyz.brassgoggledcoders.transport.item.CargoCarrierMinecartItem;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class TransportEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Transport.ID);

    public static RegistryObject<EntityType<CargoCarrierMinecartEntity>> CARGO_MINECART = ENTITIES.register("cargo_minecart",
            () -> EntityType.Builder.create(new EntityType.IFactory<CargoCarrierMinecartEntity>() {
                @Override
                @Nonnull
                @ParametersAreNonnullByDefault
                public CargoCarrierMinecartEntity create(EntityType<CargoCarrierMinecartEntity> entityType, World world) {
                    return new CargoCarrierMinecartEntity(entityType, world);
                }
            }, EntityClassification.MISC)
                    .build("cargo_minecart"));

    public static RegistryObject<CargoCarrierMinecartItem> CARGO_MINECART_ITEM = ITEMS.register("cargo_minecart",
            CargoCarrierMinecartItem::new);

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
        ITEMS.register(modBus);
    }
}

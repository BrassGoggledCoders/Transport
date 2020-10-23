package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.CargoCarrierMinecartEntity;
import xyz.brassgoggledcoders.transport.entity.HulledBoatEntity;
import xyz.brassgoggledcoders.transport.entity.LocomotiveEntity;
import xyz.brassgoggledcoders.transport.entity.ModularBoatEntity;
import xyz.brassgoggledcoders.transport.item.CargoCarrierMinecartItem;
import xyz.brassgoggledcoders.transport.item.LocomotiveItem;
import xyz.brassgoggledcoders.transport.item.ModularBoatItem;
import xyz.brassgoggledcoders.transport.renderer.minecart.LocomotiveRenderer;

public class TransportEntities {
    public static final RegistryEntry<EntityType<CargoCarrierMinecartEntity>> CARGO_MINECART = Transport.getRegistrate()
            .object("cargo_minecart")
            .<CargoCarrierMinecartEntity>entity(CargoCarrierMinecartEntity::new, EntityClassification.MISC)
            .lang("Modular Minecart")
            .properties(properties -> properties.trackingRange(8).size(0.98F, 0.7F))
            .register();

    public static RegistryEntry<EntityType<ModularBoatEntity>> MODULAR_BOAT = Transport.getRegistrate()
            .object("modular_boat")
            .<ModularBoatEntity>entity(ModularBoatEntity::new, EntityClassification.MISC)
            .properties(boatSetup())
            .lang("Modular Boat")
            .register();

    public static ItemEntry<CargoCarrierMinecartItem> CARGO_MINECART_ITEM = Transport.getRegistrate()
            .object("cargo_minecart")
            .item(CargoCarrierMinecartItem::new)
            .model((context, provider) -> {
            })
            .lang("Modular Minecart")
            .register();

    public static ItemEntry<ModularBoatItem> MODULAR_BOAT_ITEM = Transport.getRegistrate()
            .object("modular_boat")
            .item(ModularBoatItem::new)
            .model((context, provider) -> {
            })
            .lang("Modular Boat")
            .register();


    public static RegistryEntry<EntityType<HulledBoatEntity>> HULLED_BOAT = Transport.getRegistrate()
            .object("hulled_boat")
            .<HulledBoatEntity>entity(HulledBoatEntity::new, EntityClassification.MISC)
            .lang("%s Boat")
            .properties(boatSetup())
            .register();

    public static RegistryEntry<EntityType<LocomotiveEntity>> DIESEL_LOCOMOTIVE = Transport.getRegistrate()
            .object("diesel_locomotive")
            .item(LocomotiveItem::new)
            .lang("Diesel Locomotive")
            .build()
            .<LocomotiveEntity>entity(LocomotiveEntity::new, EntityClassification.MISC)
            .renderer(() -> renderManager -> new LocomotiveRenderer<>(Transport.rl("item/diesel_locomotive"),
                    Transport.rl("textures/entity/diesel_locomotive.png"), renderManager))
            .register();

    public static RegistryEntry<LocomotiveItem> DIESEL_BOAT_ITEM = Transport.getRegistrate()
            .object("diesel_boat")
            .item(LocomotiveItem::new)
            .lang("Diesel Tug")
            .register();

    public static RegistryEntry<LocomotiveItem> STEAM_LOCOMOTIVE_ITEM = Transport.getRegistrate()
            .object("steam_locomotive")
            .item(LocomotiveItem::new)
            .lang("Steam Locomotive")
            .register();

    public static RegistryEntry<LocomotiveItem> STEAM_BOAT_ITEM = Transport.getRegistrate()
            .object("steam_boat")
            .item(LocomotiveItem::new)
            .lang("Steam Tug")
            .register();

    private static <T extends Entity> NonNullConsumer<EntityType.Builder<T>> boatSetup() {
        return tBuilder -> tBuilder.trackingRange(10).size(1.375F, 0.5625F);
    }

    public static void setup() {

    }
}

package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.CargoCarrierMinecartEntity;
import xyz.brassgoggledcoders.transport.entity.HulledBoatEntity;
import xyz.brassgoggledcoders.transport.entity.ModularBoatEntity;
import xyz.brassgoggledcoders.transport.entity.TugBoatEntity;
import xyz.brassgoggledcoders.transport.entity.locomotive.DieselLocomotiveEntity;
import xyz.brassgoggledcoders.transport.entity.locomotive.SteamLocomotiveEntity;
import xyz.brassgoggledcoders.transport.item.CargoCarrierMinecartItem;
import xyz.brassgoggledcoders.transport.item.LocomotiveItem;
import xyz.brassgoggledcoders.transport.item.ModularBoatItem;
import xyz.brassgoggledcoders.transport.item.TugBoatItem;
import xyz.brassgoggledcoders.transport.renderer.boat.TugBoatRenderer;
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

    public static RegistryEntry<LocomotiveItem<DieselLocomotiveEntity>> DIESEL_LOCOMOTIVE_ITEM =
            Transport.getRegistrate()
                    .object("diesel_locomotive")
                    .item(builder -> new LocomotiveItem<>(TransportEntities.DIESEL_LOCOMOTIVE, builder))
                    .lang("Diesel Locomotive")
                    .model((context, provider) -> {
                    })
                    .register();

    public static RegistryEntry<EntityType<DieselLocomotiveEntity>> DIESEL_LOCOMOTIVE = Transport.getRegistrate()
            .object("diesel_locomotive")
            .entity(DieselLocomotiveEntity::new, EntityClassification.MISC)
            .lang("Diesel Locomotive")
            .properties(properties -> properties.trackingRange(8).size(1.2F, 1.8F))
            .renderer(() -> renderManager -> new LocomotiveRenderer<>(DIESEL_LOCOMOTIVE_ITEM, 0.085F, 5,
                    Transport.rl("textures/entity/diesel_locomotive.png"), renderManager))
            .register();

    public static RegistryEntry<TugBoatItem<TugBoatEntity>> DIESEL_BOAT_ITEM = Transport.getRegistrate()
            .object("diesel_boat")
            .item(properties -> new TugBoatItem<>(TransportEntities.DIESEL_BOAT, properties))
            .lang("Diesel Tug Boat")
            .model((context, provider) -> {
            })
            .register();

    public static RegistryEntry<EntityType<TugBoatEntity>> DIESEL_BOAT = Transport.getRegistrate()
            .object("diesel_boat")
            .<TugBoatEntity>entity(TugBoatEntity::new, EntityClassification.MISC)
            .lang("Diesel Tug Boat")
            .properties(boatSetup())
            .renderer(() -> renderManager -> new TugBoatRenderer<>(DIESEL_BOAT_ITEM, 1F,
                    Transport.rl("textures/entity/diesel_boat.png"), renderManager))
            .register();

    public static RegistryEntry<LocomotiveItem<SteamLocomotiveEntity>> STEAM_LOCOMOTIVE_ITEM =
            Transport.getRegistrate()
                    .object("steam_locomotive")
                    .item(builder -> new LocomotiveItem<>(TransportEntities.STEAM_LOCOMOTIVE, builder))
                    .lang("Steam Locomotive")
                    .model((context, provider) -> {
                    })
                    .register();

    public static RegistryEntry<EntityType<SteamLocomotiveEntity>> STEAM_LOCOMOTIVE =
            Transport.getRegistrate()
                    .object("steam_locomotive")
                    .entity(SteamLocomotiveEntity::new, EntityClassification.MISC)
                    .properties(properties -> properties.trackingRange(8).size(1.4F, 1.8F))
                    .lang("Steam Locomotive")
                    .renderer(() -> renderManager -> new LocomotiveRenderer<>(STEAM_LOCOMOTIVE_ITEM, 1F,
                            0.4F, Transport.rl("textures/entity/steam_locomotive.png"), renderManager))
                    .register();

    public static RegistryEntry<TugBoatItem<TugBoatEntity>> STEAM_BOAT_ITEM = Transport.getRegistrate()
            .object("steam_boat")
            .item(properties -> new TugBoatItem<>(TransportEntities.STEAM_BOAT, properties))
            .lang("Steam Tug Boat")
            .model((context, provider) -> {
            })
            .register();

    public static RegistryEntry<EntityType<TugBoatEntity>> STEAM_BOAT = Transport.getRegistrate()
            .object("steam_boat")
            .<TugBoatEntity>entity(TugBoatEntity::new, EntityClassification.MISC)
            .lang("Steam Tug Boat")
            .properties(properties -> properties.trackingRange(10).size(2.0F, 0.5625F))
            .renderer(() -> renderManager -> new TugBoatRenderer<>(STEAM_BOAT_ITEM, 1F,
                    Transport.rl("textures/entity/steam_boat.png"), renderManager))
            .register();

    private static <T extends Entity> NonNullConsumer<EntityType.Builder<T>> boatSetup() {
        return tBuilder -> tBuilder.trackingRange(10).size(1.375F, 0.5625F);
    }

    public static void setup() {

    }
}

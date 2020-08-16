package xyz.brassgoggledcoders.transport.immersiveengineering.compat;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.immersiveengineering.TransportIE;
import xyz.brassgoggledcoders.transport.item.HulledBoatItem;

@SuppressWarnings("unused")
public class ImmersiveEngineeringHullTypes {
    private static final DeferredRegister<HullType> HULL_TYPES = DeferredRegister.create(HullType.class, TransportIE.ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TransportIE.ID);

    public static final RegistryObject<Item> TREATED_WOOD_PLANKS = RegistryObject.of(
            new ResourceLocation("immersiveengineering", "treated_wood_planks"), ForgeRegistries.ITEMS);

    public static final RegistryObject<HullType> TREATED_WOOD = HULL_TYPES.register("treated_wood",
            () -> new HullType(() -> TREATED_WOOD_PLANKS.orElse(Items.AIR),
                    () -> new ResourceLocation(TransportIE.ID, "textures/entity/treated_wood_boat.png")));

    public static final RegistryObject<Item> TREATED_WOOD_BOAT = ITEMS.register("treated_wood_boat",
            () -> new HulledBoatItem(TREATED_WOOD, new Item.Properties().group(Transport.ITEM_GROUP)));

    public static void register(IEventBus modEventBus) {
        HULL_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}

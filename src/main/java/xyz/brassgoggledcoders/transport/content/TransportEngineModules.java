package xyz.brassgoggledcoders.transport.content;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.engine.Engine;
import xyz.brassgoggledcoders.transport.api.item.ModuleItem;
import xyz.brassgoggledcoders.transport.engine.BoosterEngineInstance;
import xyz.brassgoggledcoders.transport.engine.CreativeEngineInstance;
import xyz.brassgoggledcoders.transport.engine.SolidFuelEngineInstance;

@SuppressWarnings("unused")
public class TransportEngineModules {
    private static final DeferredRegister<Engine> ENGINES = new DeferredRegister<>(TransportAPI.ENGINES.get(), Transport.ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Transport.ID);

    public static RegistryObject<Engine> CREATIVE = ENGINES.register("creative", () -> new Engine(CreativeEngineInstance::new));
    public static RegistryObject<ModuleItem<Engine>> CREATIVE_ITEM = ITEMS.register("creative_engine",
            () -> new ModuleItem<>(CREATIVE, new Item.Properties()
                    .group(Transport.ITEM_GROUP)));

    public static RegistryObject<Engine> SOLID_FUEL = ENGINES.register("solid_fuel", () -> new Engine(SolidFuelEngineInstance::new));
    public static RegistryObject<ModuleItem<Engine>> SOLID_FUEL_ITEM = ITEMS.register("solid_fuel_engine",
            () -> new ModuleItem<>(SOLID_FUEL, new Item.Properties()
                    .group(Transport.ITEM_GROUP)));

    public static RegistryObject<Engine> BOOSTER = ENGINES.register("booster", () -> new Engine(BoosterEngineInstance::new));
    public static RegistryObject<ModuleItem<Engine>> BOOSTER_ITEM = ITEMS.register("booster_engine",
            () -> new ModuleItem<>(BOOSTER, new Item.Properties()
                    .group(Transport.ITEM_GROUP)));

    public static void register(IEventBus modBus) {
        ENGINES.register(modBus);
        ITEMS.register(modBus);
    }
}

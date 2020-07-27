package xyz.brassgoggledcoders.transport.content;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.item.ModuleItem;
import xyz.brassgoggledcoders.transport.engine.BoosterEngineModuleInstance;
import xyz.brassgoggledcoders.transport.engine.CreativeEngineModuleInstance;
import xyz.brassgoggledcoders.transport.engine.SolidFuelEngineModuleInstance;

@SuppressWarnings("unused")
public class TransportEngineModules {
    private static final DeferredRegister<EngineModule> ENGINES = DeferredRegister.create(EngineModule.class, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Transport.ID);

    public static RegistryObject<EngineModule> CREATIVE = ENGINES.register("creative", () -> new EngineModule(CreativeEngineModuleInstance::new));
    public static RegistryObject<ModuleItem<EngineModule>> CREATIVE_ITEM = ITEMS.register("creative_engine",
            () -> new ModuleItem<>(CREATIVE, new Item.Properties()
                    .group(Transport.ITEM_GROUP)));

    public static RegistryObject<EngineModule> SOLID_FUEL = ENGINES.register("solid_fuel", () -> new EngineModule(SolidFuelEngineModuleInstance::new));
    public static RegistryObject<ModuleItem<EngineModule>> SOLID_FUEL_ITEM = ITEMS.register("solid_fuel_engine",
            () -> new ModuleItem<>(SOLID_FUEL, new Item.Properties()
                    .group(Transport.ITEM_GROUP)));

    public static RegistryObject<EngineModule> BOOSTER = ENGINES.register("booster", () -> new EngineModule(BoosterEngineModuleInstance::new));
    public static RegistryObject<ModuleItem<EngineModule>> BOOSTER_ITEM = ITEMS.register("booster_engine",
            () -> new ModuleItem<>(BOOSTER, new Item.Properties()
                    .group(Transport.ITEM_GROUP)));

    public static void register(IEventBus modBus) {
        ENGINES.register(modBus);
        ITEMS.register(modBus);
    }
}

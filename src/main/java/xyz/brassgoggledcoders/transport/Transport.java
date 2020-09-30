package xyz.brassgoggledcoders.transport;

import com.hrznstudio.titanium.network.locator.LocatorType;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.bookholder.IBookHolder;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.manager.IManageable;
import xyz.brassgoggledcoders.transport.api.manager.IManager;
import xyz.brassgoggledcoders.transport.api.manager.Manageable;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParser;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateStorage;
import xyz.brassgoggledcoders.transport.api.predicate.StringPredicate;
import xyz.brassgoggledcoders.transport.compat.immersiveengineering.TransportIE;
import xyz.brassgoggledcoders.transport.compat.quark.TransportQuark;
import xyz.brassgoggledcoders.transport.compat.vanilla.TransportVanilla;
import xyz.brassgoggledcoders.transport.container.EntityLocatorInstance;
import xyz.brassgoggledcoders.transport.content.*;
import xyz.brassgoggledcoders.transport.item.TransportItemGroup;
import xyz.brassgoggledcoders.transport.nbt.CompoundNBTStorage;
import xyz.brassgoggledcoders.transport.nbt.EmptyStorage;
import xyz.brassgoggledcoders.transport.network.NetworkHandler;
import xyz.brassgoggledcoders.transport.pointmachine.ComparatorPointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.LeverPointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.PredicatePointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.RedstonePointMachineBehavior;
import xyz.brassgoggledcoders.transport.predicate.NamePredicate;
import xyz.brassgoggledcoders.transport.predicate.TimePredicate;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrate;

import java.util.function.Predicate;

import static xyz.brassgoggledcoders.transport.Transport.ID;

@Mod(ID)
public class Transport {
    public static final String ID = "transport";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final LocatorType ENTITY = new LocatorType("entity", EntityLocatorInstance::new);
    public static final Lazy<TransportRegistrate> TRANSPORT_REGISTRATE = Lazy.of(() -> TransportRegistrate.create(ID)
            .addDataGenerator(ProviderType.BLOCK_TAGS, TransportAdditionalData::generateBlockTags)
            .addDataGenerator(ProviderType.ITEM_TAGS, TransportAdditionalData::generateItemTags)
            .addDataGenerator(ProviderType.RECIPE, TransportAdditionalData::generateRecipes)
            .addDataGenerator(ProviderType.LANG, TransportAdditionalData::generateLang)
            .itemGroup(() -> new TransportItemGroup(ID, () -> TransportBlocks.HOLDING_RAIL.orElseThrow(
                    () -> new IllegalStateException("Got Item too early")
            )))
    );

    public static Transport instance;

    public final NetworkHandler networkHandler;

    public Transport() {
        instance = this;

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::newRegistry);

        this.networkHandler = new NetworkHandler();
        TransportAPI.setNetworkHandler(this.networkHandler);

        makeRegistry("module_type", ModuleType.class);
        makeRegistry("cargo", CargoModule.class);
        makeRegistry("engine", EngineModule.class);
        makeRegistry("module_slot", ModuleSlot.class);
        makeRegistry("hull_type", HullType.class);

        TransportBlocks.setup();
        TransportContainers.register(modBus);
        TransportEntities.setup();
        TransportItems.setup();
        TransportRecipes.setup();
        TransportModuleTypes.setup();
        TransportCargoModules.setup();
        TransportEngineModules.setup();
        TransportModuleSlots.setup();
        TransportHullTypes.setup();
        TransportText.setup();

        TransportVanilla.setup();
        TransportIE.setup();
        TransportQuark.setup();
    }

    public void newRegistry(RegistryEvent.NewRegistry newRegistryEvent) {

    }

    public void commonSetup(FMLCommonSetupEvent event) {
        TransportAPI.addPointMachineBehavior(Blocks.LEVER, new LeverPointMachineBehavior());
        TransportAPI.addPointMachineBehavior(Blocks.REPEATER, new RedstonePointMachineBehavior());
        TransportAPI.addPointMachineBehavior(Blocks.COMPARATOR, new ComparatorPointMachineBehavior());
        TransportAPI.addPointMachineBehavior(Blocks.LECTERN, new PredicatePointMachineBehavior());

        CapabilityManager.INSTANCE.register(PredicateStorage.class, new EmptyStorage<>(), PredicateStorage::new);
        CapabilityManager.INSTANCE.register(IModularEntity.class, new CompoundNBTStorage<>(), () -> null);
        CapabilityManager.INSTANCE.register(IManager.class, new CompoundNBTStorage<>(), () -> null);
        CapabilityManager.INSTANCE.register(IManageable.class, new CompoundNBTStorage<>(), () -> new Manageable(null));
        CapabilityManager.INSTANCE.register(IBookHolder.class, new EmptyStorage<>(), () -> null);

        TransportAPI.generateItemToModuleMap();
    }

    private static <T extends IForgeRegistryEntry<T>> void makeRegistry(String name, Class<T> type) {
        new RegistryBuilder<T>()
                .setName(new ResourceLocation("transport", name))
                .setType(type)
                .create();
    }

    public static TransportRegistrate getRegistrate() {
        return TRANSPORT_REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

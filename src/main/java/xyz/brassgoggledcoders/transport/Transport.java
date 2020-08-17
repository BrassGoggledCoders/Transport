package xyz.brassgoggledcoders.transport;

import com.hrznstudio.titanium.network.locator.LocatorType;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
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
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.api.routing.RoutingStorage;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;
import xyz.brassgoggledcoders.transport.api.routing.serializer.ListRoutingDeserializer;
import xyz.brassgoggledcoders.transport.api.routing.serializer.ListValidatedRoutingDeserializer;
import xyz.brassgoggledcoders.transport.api.routing.serializer.NoInputRoutingDeserializer;
import xyz.brassgoggledcoders.transport.api.routing.serializer.SingleRoutingDeserializer;
import xyz.brassgoggledcoders.transport.container.EntityLocatorInstance;
import xyz.brassgoggledcoders.transport.content.*;
import xyz.brassgoggledcoders.transport.datagen.TransportDataGenerator;
import xyz.brassgoggledcoders.transport.event.EventHandler;
import xyz.brassgoggledcoders.transport.item.TransportItemGroup;
import xyz.brassgoggledcoders.transport.nbt.CompoundNBTStorage;
import xyz.brassgoggledcoders.transport.nbt.EmptyStorage;
import xyz.brassgoggledcoders.transport.network.NetworkHandler;
import xyz.brassgoggledcoders.transport.pointmachine.ComparatorPointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.LeverPointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.RedstonePointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.RoutingPointMachineBehavior;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrate;
import xyz.brassgoggledcoders.transport.routing.instruction.*;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.transport.Transport.ID;

@Mod(ID)
public class Transport {
    public static final String ID = "transport";
    public static final Lazy<ItemGroup> ITEM_GROUP = Lazy.of(() -> new TransportItemGroup(ID, TransportBlocks.HOLDING_RAIL::get));
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final LocatorType ENTITY = new LocatorType("entity", EntityLocatorInstance::new);
    public static final Lazy<TransportRegistrate> TRANSPORT_REGISTRATE = Lazy.of(() -> TransportRegistrate.create(ID));
    public static Transport instance;

    public final NetworkHandler networkHandler;

    public Transport() {
        instance = this;

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(TransportDataGenerator::gather);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::newRegistry);
        MinecraftForge.EVENT_BUS.addGenericListener(TileEntity.class, EventHandler::onAttachTileEntityCapabilities);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, EventHandler::onAttachEntityCapabilities);

        this.networkHandler = new NetworkHandler();
        TransportAPI.setNetworkHandler(this.networkHandler);

        makeRegistry("module_type", ModuleType.class);
        makeRegistry("cargo", CargoModule.class);
        makeRegistry("engine", EngineModule.class);
        makeRegistry("module_slot", ModuleSlot.class);
        makeRegistry("hull_type", HullType.class);

        TransportBlocks.register(modBus);
        TransportContainers.register(modBus);
        TransportEntities.setup();
        TransportItems.setup();

        TransportModuleTypes.register(modBus);
        TransportCargoModules.register(modBus);
        TransportEngineModules.register(modBus);
        TransportModuleSlots.register(modBus);
        TransportHullTypes.setup();
    }

    public void newRegistry(RegistryEvent.NewRegistry newRegistryEvent) {

    }

    public void commonSetup(FMLCommonSetupEvent event) {
        TransportAPI.addPointMachineBehavior(Blocks.LEVER, new LeverPointMachineBehavior());
        TransportAPI.addPointMachineBehavior(Blocks.REPEATER, new RedstonePointMachineBehavior());
        TransportAPI.addPointMachineBehavior(Blocks.COMPARATOR, new ComparatorPointMachineBehavior());
        TransportAPI.addPointMachineBehavior(Blocks.LECTERN, new RoutingPointMachineBehavior());

        TransportAPI.addRoutingDeserializer("TRUE", new NoInputRoutingDeserializer(TrueRouting::new));
        TransportAPI.addRoutingDeserializer("FALSE", new NoInputRoutingDeserializer(FalseRouting::new));
        TransportAPI.addRoutingDeserializer("NAME", new ListRoutingDeserializer<>(String.class, NameRouting::new));
        TransportAPI.addRoutingDeserializer("AND", new ListRoutingDeserializer<>(Routing.class, AndRouting::new));
        TransportAPI.addRoutingDeserializer("OR", new ListRoutingDeserializer<>(Routing.class, OrRouting::new));
        TransportAPI.addRoutingDeserializer("NOT", new SingleRoutingDeserializer<>(Routing.class, NotRouting::new));
        TransportAPI.addRoutingDeserializer("RIDERS", new SingleRoutingDeserializer<>(Number.class, RiderRouting::new));
        TransportAPI.addRoutingDeserializer("POWERED", new NoInputRoutingDeserializer(PoweredRouting::new));
        TransportAPI.addRoutingDeserializer("COMPARATOR", new SingleRoutingDeserializer<>(Number.class, ComparatorRouting::new));
        TransportAPI.addRoutingDeserializer("TIME", new ListValidatedRoutingDeserializer<>(String.class, TimeRouting::create));

        CapabilityManager.INSTANCE.register(RoutingStorage.class, new EmptyStorage<>(), RoutingStorage::new);
        CapabilityManager.INSTANCE.register(IModularEntity.class, new CompoundNBTStorage<>(), () -> null);

        TransportAPI.generateItemToModuleMap();
    }

    private static <T extends IForgeRegistryEntry<T>> void makeRegistry(String name, Class<T> type) {
        new RegistryBuilder<T>()
                .setName(new ResourceLocation("transport", name))
                .setType(type)
                .create();
    }

    @Nonnull
    public static ItemGroup getItemGroup() {
        return ITEM_GROUP.get();
    }

    public static TransportRegistrate getRegistrate() {
        return TRANSPORT_REGISTRATE.get();
    }
}

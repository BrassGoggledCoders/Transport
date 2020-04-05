package xyz.brassgoggledcoders.transport;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.engine.Engine;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.api.routing.RoutingStorage;
import xyz.brassgoggledcoders.transport.api.routing.RoutingStorageProvider;
import xyz.brassgoggledcoders.transport.routing.instruction.FalseRouting;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;
import xyz.brassgoggledcoders.transport.routing.instruction.TrueRouting;
import xyz.brassgoggledcoders.transport.api.routing.serializer.NoInputRoutingDeserializer;
import xyz.brassgoggledcoders.transport.api.routing.serializer.ListRoutingDeserializer;
import xyz.brassgoggledcoders.transport.api.routing.serializer.SingleRoutingDeserializer;
import xyz.brassgoggledcoders.transport.content.*;
import xyz.brassgoggledcoders.transport.datagen.TransportDataGenerator;
import xyz.brassgoggledcoders.transport.item.TransportItemGroup;
import xyz.brassgoggledcoders.transport.pointmachine.ComparatorPointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.LeverPointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.RedstonePointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.RoutingPointMachineBehavior;
import xyz.brassgoggledcoders.transport.routing.instruction.*;

import javax.annotation.Nullable;

import static xyz.brassgoggledcoders.transport.Transport.ID;

@Mod(ID)
public class Transport {
    public static final String ID = "transport";
    public static final ItemGroup ITEM_GROUP = new TransportItemGroup(ID, TransportBlocks.HOLDING_RAIL::getItem);

    public static Transport instance;

    public Transport() {
        instance = this;

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(ClientEventHandler::clientSetup));
        modBus.addListener(TransportDataGenerator::gather);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::newRegistry);
        MinecraftForge.EVENT_BUS.addGenericListener(TileEntity.class, this::attachCapability);
    }

    public void attachCapability(AttachCapabilitiesEvent<TileEntity> attachCapabilitiesEvent) {
        if (attachCapabilitiesEvent.getObject() instanceof LecternTileEntity) {
            attachCapabilitiesEvent.addCapability(new ResourceLocation(ID, "routing_storage"),
                    new RoutingStorageProvider());
        }
    }

    public void newRegistry(RegistryEvent.NewRegistry newRegistryEvent) {
        //noinspection unchecked
        makeRegistry("module_type", ModuleType.class);
        makeRegistry("cargo", Cargo.class);
        makeRegistry("engine", Engine.class);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        TransportBlocks.register(modBus);
        TransportContainers.register(modBus);
        TransportEntities.register(modBus);
        TransportRecipes.register(modBus);
        TransportItems.register(modBus);

        TransportModuleTypes.register(modBus);
        TransportCargoModules.register(modBus);
        TransportEngineModules.register(modBus);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        TransportAPI.POINT_MACHINE_BEHAVIORS.put(Blocks.LEVER, new LeverPointMachineBehavior());
        TransportAPI.POINT_MACHINE_BEHAVIORS.put(Blocks.REPEATER, new RedstonePointMachineBehavior());
        TransportAPI.POINT_MACHINE_BEHAVIORS.put(Blocks.COMPARATOR, new ComparatorPointMachineBehavior());
        TransportAPI.POINT_MACHINE_BEHAVIORS.put(Blocks.LECTERN, new RoutingPointMachineBehavior());

        TransportAPI.addRoutingDeserializer("TRUE", new NoInputRoutingDeserializer(TrueRouting::new));
        TransportAPI.addRoutingDeserializer("FALSE", new NoInputRoutingDeserializer(FalseRouting::new));
        TransportAPI.addRoutingDeserializer("NAME", new ListRoutingDeserializer<>(String.class, NameRouting::new));
        TransportAPI.addRoutingDeserializer("AND", new ListRoutingDeserializer<>(Routing.class, AndRouting::new));
        TransportAPI.addRoutingDeserializer("OR", new ListRoutingDeserializer<>(Routing.class, OrRouting::new));
        TransportAPI.addRoutingDeserializer("NOT", new SingleRoutingDeserializer<>(Routing.class, NotRouting::new));
        TransportAPI.addRoutingDeserializer("RIDERS", new SingleRoutingDeserializer<>(Number.class, RiderRouting::new));
        TransportAPI.addRoutingDeserializer("POWERED", new NoInputRoutingDeserializer(PoweredRouting::new));

        CapabilityManager.INSTANCE.register(RoutingStorage.class, new Capability.IStorage<RoutingStorage>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<RoutingStorage> capability, RoutingStorage instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<RoutingStorage> capability, RoutingStorage instance, Direction side, INBT nbt) {

            }
        }, RoutingStorage::new);
    }

    private static <T extends IForgeRegistryEntry<T>> void makeRegistry(String name, Class<T> type) {
        new RegistryBuilder<T>()
                .setName(new ResourceLocation("transport", name))
                .setType(type)
                .create();
    }
}

package xyz.brassgoggledcoders.transport;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
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
import xyz.brassgoggledcoders.transport.content.*;
import xyz.brassgoggledcoders.transport.datagen.TransportDataGenerator;
import xyz.brassgoggledcoders.transport.item.TransportItemGroup;
import xyz.brassgoggledcoders.transport.pointmachine.ComparatorPointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.LeverPointMachineBehavior;
import xyz.brassgoggledcoders.transport.pointmachine.RedstonePointMachineBehavior;

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
    }

    private static <T extends IForgeRegistryEntry<T>> void makeRegistry(String name, Class<T> type) {
        new RegistryBuilder<T>()
                .setName(new ResourceLocation("transport", name))
                .setType(type)
                .create();
    }
}

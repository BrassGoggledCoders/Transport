package xyz.brassgoggledcoders.transport;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.content.*;
import xyz.brassgoggledcoders.transport.entity.ResourceLocationDataSerializer;
import xyz.brassgoggledcoders.transport.item.TransportItemGroup;
import xyz.brassgoggledcoders.transport.nbt.NBTStorage;
import xyz.brassgoggledcoders.transport.provider.TransportDataGenerator;
import xyz.brassgoggledcoders.transport.renderer.CargoCarrierMinecartEntityRenderer;
import xyz.brassgoggledcoders.transport.screen.CargoScreen;
import xyz.brassgoggledcoders.transport.screen.LoaderScreen;

import static xyz.brassgoggledcoders.transport.Transport.ID;

@Mod(ID)
public class Transport {
    public static final String ID = "transport";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static final ItemGroup ITEM_GROUP = new TransportItemGroup(ID, TransportBlocks.HOLDING_RAIL::getItem);
    public static final ResourceLocationDataSerializer RESOURCE_LOCATION_DATA_SERIALIZER = createDataSerializer();

    public static Transport instance;

    public Transport() {
        instance = this;

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::clientSetup);
        modBus.addListener(TransportDataGenerator::gather);

        TransportBlocks.register(modBus);
        TransportCargoes.register(modBus);
        TransportContainers.register(modBus);
        TransportEntities.register(modBus);
        TransportRecipes.register(modBus);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(TransportBlocks.HOLDING_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.DIAMOND_CROSSING_RAIL.getBlock(), RenderType.getCutout());

        ScreenManager.registerFactory(TransportContainers.LOADER.get(), LoaderScreen::new);
        ScreenManager.registerFactory(TransportContainers.CARGO.get(), CargoScreen::new);

        Minecraft.getInstance().getRenderManager().register(TransportEntities.CARGO_MINECART.get(),
                new CargoCarrierMinecartEntityRenderer(Minecraft.getInstance().getRenderManager()));
    }

    private static ResourceLocationDataSerializer createDataSerializer() {
        ResourceLocationDataSerializer dataSerializer = new ResourceLocationDataSerializer();
        DataSerializers.registerSerializer(dataSerializer);
        return dataSerializer;
    }
}

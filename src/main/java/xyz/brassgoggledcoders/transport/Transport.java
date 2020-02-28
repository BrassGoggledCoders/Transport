package xyz.brassgoggledcoders.transport;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.CargoCarrierEmpty;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.entity.ResourceLocationDataSerializer;
import xyz.brassgoggledcoders.transport.item.TransportItemGroup;
import xyz.brassgoggledcoders.transport.nbt.NBTStorage;
import xyz.brassgoggledcoders.transport.provider.TransportDataGenerator;
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

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
        modBus.addListener(TransportDataGenerator::gather);

        TransportBlocks.register(modBus);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ICargoCarrier.class, new NBTStorage<>(), CargoCarrierEmpty::new);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(TransportBlocks.HOLDING_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.DIAMOND_CROSSING_RAIL.getBlock(), RenderType.getCutout());

        ScreenManager.registerFactory(TransportBlocks.LOADER_CONTAINER.get(), LoaderScreen::new);
    }

    private static ResourceLocationDataSerializer createDataSerializer() {
        ResourceLocationDataSerializer dataSerializer = new ResourceLocationDataSerializer();
        DataSerializers.registerSerializer(dataSerializer);
        return dataSerializer;
    }
}

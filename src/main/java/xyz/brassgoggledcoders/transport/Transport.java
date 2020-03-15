package xyz.brassgoggledcoders.transport;

import net.minecraft.item.ItemGroup;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.transport.content.*;
import xyz.brassgoggledcoders.transport.datagen.TransportDataGenerator;
import xyz.brassgoggledcoders.transport.entity.ResourceLocationDataSerializer;
import xyz.brassgoggledcoders.transport.item.TransportItemGroup;

import static xyz.brassgoggledcoders.transport.Transport.ID;

@Mod(ID)
public class Transport {
    public static final String ID = "transport";
    public static final ItemGroup ITEM_GROUP = new TransportItemGroup(ID, TransportBlocks.HOLDING_RAIL::getItem);
    public static final ResourceLocationDataSerializer RESOURCE_LOCATION_DATA_SERIALIZER = createDataSerializer();

    public static Transport instance;

    public Transport() {
        instance = this;

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(ClientEventHandler::clientSetup));
        modBus.addListener(TransportDataGenerator::gather);

        TransportBlocks.register(modBus);
        TransportCargoes.register(modBus);
        TransportContainers.register(modBus);
        TransportEntities.register(modBus);
        TransportRecipes.register(modBus);
    }

    private static ResourceLocationDataSerializer createDataSerializer() {
        ResourceLocationDataSerializer dataSerializer = new ResourceLocationDataSerializer();
        DataSerializers.registerSerializer(dataSerializer);
        return dataSerializer;
    }
}

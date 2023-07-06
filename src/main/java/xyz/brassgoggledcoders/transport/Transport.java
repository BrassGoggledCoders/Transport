package xyz.brassgoggledcoders.transport;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.content.*;
import xyz.brassgoggledcoders.transport.item.TransportCreativeModeTab;
import xyz.brassgoggledcoders.transport.network.NetworkHandler;

import static xyz.brassgoggledcoders.transport.Transport.ID;

@Mod(ID)
public class Transport {
    public static final String ID = "transport";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final NonNullLazy<Registrate> TRANSPORT_REGISTRATE = NonNullLazy.of(() ->
            Registrate.create(ID)
                    .creativeModeTab(TransportCreativeModeTab::new, "Transport")
                    .addDataGenerator(ProviderType.ITEM_TAGS, TransportAdditionalData::vanillaItemTags)
                    .addDataGenerator(ProviderType.RECIPE, TransportAdditionalData::vanillaRecipes)
    );

    public static final NetworkHandler NETWORK = new NetworkHandler();

    public Transport() {
        TransportBlocks.setup();
        TransportContainers.setup();
        TransportEntities.setup();
        TransportItems.setup();
        TransportRecipes.setup();
        TransportShellContent.setup();
        TransportText.setup();
    }

    public static Registrate getRegistrate() {
        return TRANSPORT_REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

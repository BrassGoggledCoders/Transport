package xyz.brassgoggledcoders.transport;

import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportShellContentTypes;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.transport.Transport.ID;

@Mod(ID)
public class Transport {
    public static final String ID = "transport";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final NonNullLazy<CreativeModeTab> CREATIVE_TAB = NonNullLazy.of(() ->
            new CreativeModeTab(ID) {
                @Override
                @Nonnull
                public ItemStack makeIcon() {
                    return new ItemStack(Items.MINECART);
                }
            }
    );

    public static final NonNullLazy<Registrate> TRANSPORT_REGISTRATE = NonNullLazy.of(() ->
            Registrate.create(ID)
                    .creativeModeTab(CREATIVE_TAB::get, "Transport")
    );

    public Transport() {
        TransportBlocks.setup();
        TransportEntities.setup();
        TransportShellContentTypes.setup();
    }

    public static Registrate getRegistrate() {
        return TRANSPORT_REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

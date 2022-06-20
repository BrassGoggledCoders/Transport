package xyz.brassgoggledcoders.transport.littlelogistics;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.transport.littlelogistics.content.TransportLLEntities;
import xyz.brassgoggledcoders.transport.littlelogistics.content.TransportLLItems;
import xyz.brassgoggledcoders.transport.littlelogistics.content.TransportLLData;

import java.util.function.Supplier;

@Mod(TransportLL.ID)
public class TransportLL {
    public static final String ID = "transport_little_logistics";

    private static final Supplier<Registrate> REGISTRATE = Suppliers.memoize(() -> Registrate.create(ID)
            .addDataGenerator(ProviderType.RECIPE, TransportLLData::generateRecipes)
            .addDataGenerator(ProviderType.ITEM_TAGS, TransportLLData::generateItemTags)
    );

    public TransportLL() {
        TransportLLEntities.setup();
        TransportLLItems.setup();
    }
    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

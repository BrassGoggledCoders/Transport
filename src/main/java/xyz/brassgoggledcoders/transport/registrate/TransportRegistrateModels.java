package xyz.brassgoggledcoders.transport.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class TransportRegistrateModels {

    public static NonNullBiConsumer<DataGenContext<Item, BlockItem>, RegistrateItemModelProvider> railItem() {
        return (context, provider) -> provider.generated(context, provider.modLoc("block/rail/" + context.getName()));
    }

    public static NonNullBiConsumer<DataGenContext<Item, BlockItem>, RegistrateItemModelProvider> railItem(String name) {
        return (context, provider) -> provider.generated(context, provider.modLoc("block/rail/" + name));
    }
}

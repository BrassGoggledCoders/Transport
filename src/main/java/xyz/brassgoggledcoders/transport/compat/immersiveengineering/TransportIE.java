package xyz.brassgoggledcoders.transport.compat.immersiveengineering;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.item.HulledBoatItem;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrateRecipes;

@SuppressWarnings("unused")
public class TransportIE {
    public static final ITag.INamedTag<Item> TREATED_WOOD_TAG = ItemTags.makeWrapperTag("forge:treated_wood");

    public static RegistryEntry<HullType> TREATED_WOOD_HULL = Transport.getRegistrate()
            .object("treated_wood")
            .hullType(itemSupplier -> new HullType(itemSupplier, Transport.rl("treated_wood_boat.png")))
            .lang("Treated Wood Hull")
            .defaultRecipe(TransportEntities.MODULAR_BOAT_ITEM::get)
            .item("boat", HulledBoatItem::new, itemBuilder -> itemBuilder
                    .group(Transport::getItemGroup)
                    .tag(TransportItemTags.BOAT_HULL)
                    .model((context, modelProvider) -> modelProvider.generated(context))
                    .recipe(TransportRegistrateRecipes.vehicleShape(TREATED_WOOD_TAG)))
            .register();

    public static void setup() {
    }
}
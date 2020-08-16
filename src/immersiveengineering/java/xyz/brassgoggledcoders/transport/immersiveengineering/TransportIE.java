package xyz.brassgoggledcoders.transport.immersiveengineering;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.item.HulledBoatItem;
import xyz.brassgoggledcoders.transport.registrate.RegistrateRecipes;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrate;

@Mod(TransportIE.ID)
public class TransportIE {
    public static final String ID = "transport_ie";

    public static final ITag.INamedTag<Item> TREATED_WOOD_TAG = ItemTags.makeWrapperTag("forge:treated_wood");

    public TransportIE() {
        TransportRegistrate registrate = TransportRegistrate.create(ID);
        registrate.object("treated_wood")
                .hullType(itemSupplier -> new HullType(itemSupplier, rl("treated_wood_boat.png")))
                .lang("Treated Wood")
                .item("boat", HulledBoatItem::new, itemBuilder -> itemBuilder
                        .group(Transport::getItemGroup)
                        .model((context, modelProvider) -> modelProvider.generated(context))
                        .recipe(RegistrateRecipes.vehicleShape(TREATED_WOOD_TAG)))
                .register();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

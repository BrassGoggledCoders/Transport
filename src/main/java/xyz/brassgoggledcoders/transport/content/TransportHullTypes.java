package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.hulltype.VanillaBoatHullType;

@SuppressWarnings("unused")
public class TransportHullTypes {
    public static final RegistryEntry<VanillaBoatHullType> OAK = Transport.getRegistrate().object("oak")
            .hullType(itemSupplier -> new VanillaBoatHullType(itemSupplier, BoatEntity.Type.OAK))
            .lang("Oak Hull")
            .item(() -> Items.OAK_BOAT)
            .defaultRecipe(TransportEntities.MODULAR_BOAT_ITEM::get)
            .itemTag(TransportItemTags.HULLS_BOAT)
            .register();

    public static final RegistryEntry<VanillaBoatHullType> ACACIA = Transport.getRegistrate().object("acacia")
            .hullType(itemSupplier -> new VanillaBoatHullType(itemSupplier, BoatEntity.Type.ACACIA))
            .lang("Acacia Hull")
            .item(() -> Items.ACACIA_BOAT)
            .defaultRecipe(TransportEntities.MODULAR_BOAT_ITEM::get)
            .itemTag(TransportItemTags.HULLS_BOAT)
            .register();

    public static final RegistryEntry<VanillaBoatHullType> BIRCH = Transport.getRegistrate().object("birch")
            .hullType(itemSupplier -> new VanillaBoatHullType(itemSupplier, BoatEntity.Type.BIRCH))
            .lang("Birch Hull")
            .item(() -> Items.BIRCH_BOAT)
            .defaultRecipe(TransportEntities.MODULAR_BOAT_ITEM::get)
            .itemTag(TransportItemTags.HULLS_BOAT)
            .register();

    public static final RegistryEntry<VanillaBoatHullType> DARK_OAK = Transport.getRegistrate().object("dark_oak")
            .hullType(itemSupplier -> new VanillaBoatHullType(itemSupplier, BoatEntity.Type.DARK_OAK))
            .lang("Dark Oak Hull")
            .item(() -> Items.DARK_OAK_BOAT)
            .defaultRecipe(TransportEntities.MODULAR_BOAT_ITEM::get)
            .itemTag(TransportItemTags.HULLS_BOAT)
            .register();

    public static final RegistryEntry<VanillaBoatHullType> JUNGLE = Transport.getRegistrate().object("jungle")
            .hullType(itemSupplier -> new VanillaBoatHullType(itemSupplier, BoatEntity.Type.JUNGLE))
            .lang("Jungle Hull")
            .item(() -> Items.JUNGLE_BOAT)
            .defaultRecipe(TransportEntities.MODULAR_BOAT_ITEM::get)
            .itemTag(TransportItemTags.HULLS_BOAT)
            .register();

    public static final RegistryEntry<VanillaBoatHullType> SPRUCE = Transport.getRegistrate().object("spruce")
            .hullType(itemSupplier -> new VanillaBoatHullType(itemSupplier, BoatEntity.Type.SPRUCE))
            .lang("Spruce Hull")
            .item(() -> Items.SPRUCE_BOAT)
            .defaultRecipe(TransportEntities.MODULAR_BOAT_ITEM::get)
            .itemTag(TransportItemTags.HULLS_BOAT)
            .register();

    public static final RegistryEntry<HullType> IRON = Transport.getRegistrate().object("iron")
            .hullType(itemSupplier -> new HullType(itemSupplier, new ResourceLocation("minecart.png")))
            .lang("Iron Hull")
            .item(() -> Items.MINECART)
            .defaultRecipe(TransportEntities.CARGO_MINECART_ITEM::get)
            .itemTag(TransportItemTags.HULLS_MINECART)
            .register();

    public static void setup() {
    }
}

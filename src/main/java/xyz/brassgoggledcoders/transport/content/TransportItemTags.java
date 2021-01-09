package xyz.brassgoggledcoders.transport.content;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;

public class TransportItemTags {
    public static final ITag.INamedTag<Item> WRENCHES = forgeTag("wrenches");

    public static final ITag.INamedTag<Item> HULLS = transportTag("hulls");
    public static final ITag.INamedTag<Item> HULLS_BOAT = transportTag("hulls/boat");
    public static final ITag.INamedTag<Item> HULLS_MINECART = transportTag("hulls/minecart");

    public static final ITag.INamedTag<Item> RAILS_REGULAR = transportTag("rails/regular");
    public static final ITag.INamedTag<Item> RAILS_POWERED = transportTag("rails/powered");
    public static final ITag.INamedTag<Item> RAILS_STRUCTURE = transportTag("rails/structure");

    public static final ITag.INamedTag<Item> RAILS = forgeTag("rails");
    public static final ITag.INamedTag<Item> RAILS_IRON = forgeTag("rails/iron");
    public static final ITag.INamedTag<Item> RAILS_GOLD = forgeTag("rails/gold");

    public static final ITag.INamedTag<Item> STORAGE_BLOCKS_STEEL = forgeTag("storage_blocks/steel");

    public static ITag.INamedTag<Item> transportTag(String path) {
        return ItemTags.createOptional(Transport.rl(path));
    }

    public static ITag.INamedTag<Item> forgeTag(String path) {
        return ItemTags.createOptional(new ResourceLocation("forge", path));
    }
}

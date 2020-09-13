package xyz.brassgoggledcoders.transport.content;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class TransportItemTags {
    public static final ITag.INamedTag<Item> WRENCHES = ItemTags.makeWrapperTag("forge:wrenches");
    public static final ITag.INamedTag<Item> BOAT_HULL = ItemTags.makeWrapperTag("transport:hulls/boat");
    public static final ITag.INamedTag<Item> MINECART_HULL = ItemTags.makeWrapperTag("transport:hulls/minecart");
    public static final ITag.INamedTag<Item> REGULAR_RAILS = ItemTags.makeWrapperTag("forge:rails/regular");
    public static final ITag.INamedTag<Item> POWERED_RAILS = ItemTags.makeWrapperTag("forge:rails/powered");
    public static final ITag.INamedTag<Item> STRUCTURE_RAILS = ItemTags.makeWrapperTag("forge:rails/structure");
}

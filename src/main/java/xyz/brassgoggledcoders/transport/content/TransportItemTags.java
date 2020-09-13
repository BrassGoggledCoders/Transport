package xyz.brassgoggledcoders.transport.content;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class TransportItemTags {
    public static final ITag.INamedTag<Item> WRENCHES = ItemTags.makeWrapperTag("forge:wrenches");

    public static final ITag.INamedTag<Item> HULLS = ItemTags.makeWrapperTag("transport:hulls");
    public static final ITag.INamedTag<Item> HULLS_BOAT = ItemTags.makeWrapperTag("transport:hulls/boat");
    public static final ITag.INamedTag<Item> HULLS_MINECART = ItemTags.makeWrapperTag("transport:hulls/minecart");

    public static final ITag.INamedTag<Item> RAILS_REGULAR = ItemTags.makeWrapperTag("transport:rails/regular");
    public static final ITag.INamedTag<Item> RAILS_POWERED = ItemTags.makeWrapperTag("transport:rails/powered");
    public static final ITag.INamedTag<Item> RAILS_STRUCTURE = ItemTags.makeWrapperTag("transport:rails/structure");

}

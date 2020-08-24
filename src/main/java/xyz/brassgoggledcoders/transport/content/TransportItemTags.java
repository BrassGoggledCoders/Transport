package xyz.brassgoggledcoders.transport.content;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class TransportItemTags {
    public static final ITag.INamedTag<Item> WRENCHES = ItemTags.makeWrapperTag("forge:wrenches");
    public static final ITag.INamedTag<Item> BOAT_HULL = ItemTags.makeWrapperTag("transport:hulls/boat");
    public static final ITag.INamedTag<Item> MINECART_HULL = ItemTags.makeWrapperTag("transport:hulls/minecart");
}

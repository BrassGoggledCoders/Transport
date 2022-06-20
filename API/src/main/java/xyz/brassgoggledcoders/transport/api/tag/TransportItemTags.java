package xyz.brassgoggledcoders.transport.api.tag;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import xyz.brassgoggledcoders.transport.api.TransportAPI;

public class TransportItemTags {
    public static final TagKey<Item> RAILS_IRON = ItemTags.create(TransportAPI.rl("rails/iron"));
    public static final TagKey<Item> RAILS_GOLD = ItemTags.create(TransportAPI.rl("rails/gold"));
    public static final TagKey<Item> RAILS_COPPER = ItemTags.create(TransportAPI.rl("rails/copper"));
    public static final TagKey<Item> RAIL_PROVIDERS = ItemTags.create(TransportAPI.rl("rail_providers"));
}

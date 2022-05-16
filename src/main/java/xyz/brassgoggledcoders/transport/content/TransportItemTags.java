package xyz.brassgoggledcoders.transport.content;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import xyz.brassgoggledcoders.transport.Transport;

public class TransportItemTags {
    public static final TagKey<Item> RAILS_IRON = ItemTags.create(Transport.rl("rails/iron"));
    public static final TagKey<Item> RAILS_GOLD = ItemTags.create(Transport.rl("rails/gold"));
    public static final TagKey<Item> RAIL_PROVIDERS = ItemTags.create(Transport.rl("rail_providers"));
}

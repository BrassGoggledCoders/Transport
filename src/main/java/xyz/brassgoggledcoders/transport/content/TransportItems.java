package xyz.brassgoggledcoders.transport.content;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.entity.ShellMinecart;
import xyz.brassgoggledcoders.transport.item.PatternedRailLayerItem;
import xyz.brassgoggledcoders.transport.item.RailBreakerItem;

@SuppressWarnings("unused")
public class TransportItems {
    public static ItemEntry<Item> SHELL_MINECART = Transport.getRegistering()
            .object("shell_minecart")
            .item(properties -> TransportAPI.ITEM_HELPER.get()
                    .createShellMinecartItem((itemStack, level, pos) -> {
                        CompoundTag shellContentTag = itemStack.getTagElement(ShellContentCreatorInfo.NBT_TAG_ELEMENT);
                        ShellContent shellContent = TransportAPI.SHELL_CONTENT_CREATOR.get().create(shellContentTag);

                        return new ShellMinecart(TransportEntities.SHELL_MINECART.get(), level, pos, shellContent);
                    })
                    .apply(properties)
            )
            .withProperties(properties -> properties.stacksTo(1))
            .register();

    public static ItemEntry<PatternedRailLayerItem> PATTERNED_RAIL_LAYER = Transport.getRegistering()
            .object("patterned_rail_layer")
            .item(PatternedRailLayerItem::new)
            .withProperties(properties -> properties.stacksTo(1))
            .register();

    public static ItemEntry<RailBreakerItem> RAIL_BREAKER = Transport.getRegistering()
            .object("rail_breaker")
            .item(RailBreakerItem::new)
            .register();

    public static void setup() {

    }
}

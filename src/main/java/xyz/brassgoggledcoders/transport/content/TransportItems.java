package xyz.brassgoggledcoders.transport.content;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.attachment.ShellContentItemAttachment;
import xyz.brassgoggledcoders.transport.entity.ShellMinecart;
import xyz.brassgoggledcoders.transport.item.PatternedRailLayerItem;
import xyz.brassgoggledcoders.transport.item.RailBreakerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class TransportItems {
    public static ItemEntry<Item> SHELL_MINECART = Transport.getRegistering()
            .object("shell_minecart")
            .item(properties -> TransportAPI.ITEM_HELPER.get()
                    .createShellMinecartItem((itemStack, level, pos) -> {
                        ShellContentItemAttachment attachment = itemStack.getData(TransportAttachments.SHELL_CONTENT_ITEM);
                        ShellContent shellContent = TransportAPI.SHELL_CONTENT_CREATOR.get()
                                .create(
                                        attachment.id(),
                                        attachment.savedTag()
                                                .orElse(null)
                                );

                        return new ShellMinecart(TransportEntities.SHELL_MINECART.get(), level, pos, shellContent);
                    })
                    .apply(properties)
            )
            .withProperties(properties -> properties.stacksTo(1))
            .withCreativeTabs(TransportCreativeTabs.CREATIVE_TAB.getKey())
            .withCreateStackFunction((creativeTab, item) -> {
                Set<ResourceLocation> shellContentIds = TransportAPI.SHELL_CONTENT_CREATOR.get()
                        .getMap()
                        .keySet();
                List<ItemStack> stackList = new ArrayList<>();
                for (ResourceLocation key : shellContentIds) {
                    ItemStack itemStack = new ItemStack(item);
                    itemStack.setData(
                            TransportAttachments.SHELL_CONTENT_ITEM,
                            new ShellContentItemAttachment(key)
                    );
                    stackList.add(itemStack);
                }

                return stackList;
            })
            .register();

    public static ItemEntry<PatternedRailLayerItem> PATTERNED_RAIL_LAYER = Transport.getRegistering()
            .object("patterned_rail_layer")
            .item(PatternedRailLayerItem::new)
            .withProperties(properties -> properties.stacksTo(1))
            .withCreativeTabs(TransportCreativeTabs.CREATIVE_TAB.getKey())
            .register();

    public static ItemEntry<RailBreakerItem> RAIL_BREAKER = Transport.getRegistering()
            .object("rail_breaker")
            .item(RailBreakerItem::new)
            .withCreativeTabs(TransportCreativeTabs.CREATIVE_TAB.getKey())
            .register();

    public static void setup() {

    }
}

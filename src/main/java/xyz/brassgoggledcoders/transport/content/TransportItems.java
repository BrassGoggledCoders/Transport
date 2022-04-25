package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.item.PatternedRailLayerItem;
import xyz.brassgoggledcoders.transport.item.ShellMinecartItem;
import xyz.brassgoggledcoders.transport.model.patternedraillayer.PatternedRailLayerCustomLoaderBuilder;
import xyz.brassgoggledcoders.transport.recipe.shellitem.ShellItemRecipeBuilder;

public class TransportItems {
    public static ItemEntry<ShellMinecartItem> SHELL_MINECART = Transport.getRegistrate()
            .object("shell_minecart")
            .item(ShellMinecartItem::new)
            .properties(properties -> properties.stacksTo(8))
            .model((context, provider) -> provider.generated(context, provider.mcLoc("item/minecart")))
            .recipe((context, provider) -> ShellItemRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(Items.MINECART))
                    .save(provider, context.getId())
            )
            .properties(properties -> properties.stacksTo(1))
            .register();

    public static ItemEntry<PatternedRailLayerItem> PATTERNED_RAIL_LAYER = Transport.getRegistrate()
            .object("patterned_rail_layer")
            .item(PatternedRailLayerItem::new)
            .properties(properties -> properties.stacksTo(1))
            .model((context, provider) -> provider.getBuilder("item/patterned_rail_layer")
                    .customLoader(PatternedRailLayerCustomLoaderBuilder::new)
            )
            .register();

    public static void setup() {

    }
}

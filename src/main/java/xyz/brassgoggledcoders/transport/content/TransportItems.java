package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.item.ShellMinecartItem;
import xyz.brassgoggledcoders.transport.recipe.shellitem.ShellItemRecipeBuilder;

public class TransportItems {

    public static ItemEntry<ShellMinecartItem> SHELL_MINECART = Transport.getRegistrate()
            .object("shell_minecart")
            .item(ShellMinecartItem::new)
            .properties(properties -> properties.stacksTo(8))
            .model((context, provider) -> provider.generated(context, provider.mcLoc("item/minecart")))
            .recipe((context, provider) -> ShellItemRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(Items.MINECART))
                    .withGlue(Ingredient.of(Tags.Items.SLIMEBALLS))
                    .withGlueOptional(true)
                    .save(provider, context.getId())
            )
            .properties(properties -> properties.stacksTo(1))
            .register();

    public static void setup() {

    }
}

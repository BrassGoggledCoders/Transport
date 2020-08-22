package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.item.RailBreakerItem;

@SuppressWarnings("unused")
public class TransportItems {
    public static final ItemEntry<RailBreakerItem> RAIL_BREAKER = Transport.getRegistrate()
            .object("rail_breaker")
            .item(RailBreakerItem::new)
            .group(Transport::getItemGroup)
            .properties(properties -> properties.maxStackSize(1))
            .lang("Rail Breaker")
            .tag(TransportItemTags.WRENCHES)
            .recipe((context, recipeProvider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                    .patternLine(" RI")
                    .patternLine("RIR")
                    .patternLine("IR ")
                    .key('R', Tags.Items.DYES_RED)
                    .key('I', Tags.Items.INGOTS_IRON)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_IRON))
                    .build(recipeProvider))
            .register();

    public static void setup() {

    }
}

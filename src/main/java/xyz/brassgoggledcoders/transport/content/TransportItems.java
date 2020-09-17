package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.item.RailBreakerItem;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrateRecipes;

public class TransportItems {
    public static final ItemEntry<RailBreakerItem> RAIL_BREAKER = Transport.getRegistrate()
            .object("rail_breaker")
            .item(RailBreakerItem::new)
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

    public static final ItemEntry<Item> IRON_RAIL = Transport.getRegistrate()
            .object("iron_rail")
            .item(Item::new)
            .lang("Iron Rail")
            .tag(TransportItemTags.RAILS_IRON)
            .recipe(TransportRegistrateRecipes.railRecipes(Tags.Items.INGOTS_IRON, Items.RAIL, 64, false))
            .register();

    public static final ItemEntry<Item> GOLD_RAIL = Transport.getRegistrate()
            .object("gold_rail")
            .item(Item::new)
            .lang("Gold Rail")
            .tag(TransportItemTags.RAILS_GOLD)
            .recipe(TransportRegistrateRecipes.railRecipes(Tags.Items.INGOTS_GOLD, Items.POWERED_RAIL, 24, true))
            .register();

    public static void setup() {

    }
}

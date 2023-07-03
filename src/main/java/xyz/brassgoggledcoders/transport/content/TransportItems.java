package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.tag.TransportItemTags;
import xyz.brassgoggledcoders.transport.data.recipe.RailWorkerBenchRecipeBuilder;
import xyz.brassgoggledcoders.transport.data.recipe.ShellItemRecipeBuilder;
import xyz.brassgoggledcoders.transport.entity.ShellMinecart;
import xyz.brassgoggledcoders.transport.item.PatternedRailLayerItem;
import xyz.brassgoggledcoders.transport.item.RailBreakerItem;
import xyz.brassgoggledcoders.transport.model.patternedraillayer.PatternedRailLayerCustomLoaderBuilder;

@SuppressWarnings("unused")
public class TransportItems {
    public static ItemEntry<Item> SHELL_MINECART = Transport.getRegistrate()
            .object("shell_minecart")
            .item(properties -> TransportAPI.ITEM_HELPER.get()
                    .createShellMinecartItem((itemStack, level, pos) -> {
                        CompoundTag shellContentTag = itemStack.getTagElement("shellContent");
                        ShellContent shellContent = TransportAPI.SHELL_CONTENT_CREATOR.get().create(shellContentTag);

                        return new ShellMinecart(TransportEntities.SHELL_MINECART.get(), level, pos, shellContent);
                    })
                    .apply(properties)
            )
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
            .tag(TransportItemTags.RAIL_PROVIDERS)
            .model((context, provider) -> provider.getBuilder("item/patterned_rail_layer")
                    .parent(provider.getExistingFile(provider.mcLoc("item/generated")))
                    .customLoader(PatternedRailLayerCustomLoaderBuilder::new)
                    .withLayer(provider.mcLoc("block/smooth_stone"))
            )
            .recipe((context, provider) -> RailWorkerBenchRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(Items.SMOOTH_STONE_SLAB))
                    .withSecondaryInput(Ingredient.of(Tags.Items.CHESTS))
                    .save(provider, context.getId())
            )
            .register();

    public static ItemEntry<RailBreakerItem> RAIL_BREAKER = Transport.getRegistrate()
            .object("rail_breaker")
            .item(RailBreakerItem::new)
            .recipe((context, recipeProvider) -> ShapedRecipeBuilder.shaped(context.get())
                    .pattern(" RI")
                    .pattern("RIR")
                    .pattern("IR ")
                    .define('R', Tags.Items.DYES_RED)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(Tags.Items.INGOTS_IRON))
                    .save(recipeProvider))
            .register();

    public static void setup() {

    }
}

package xyz.brassgoggledcoders.transport.data.content;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderTypes;
import xyz.brassgoggledcoders.transport.api.tag.TransportItemTags;
import xyz.brassgoggledcoders.transport.content.TransportItems;
import xyz.brassgoggledcoders.transport.data.recipe.ShellItemRecipeBuilder;
import xyz.brassgoggledcoders.transport.data.util.ItemBasics;
import xyz.brassgoggledcoders.transport.model.patternedraillayer.PatternedRailLayerCustomLoaderBuilder;

public class TransportItemData {
    public static void generate(DataRegistering dataRegistering) {
        dataRegistering.forEntry(TransportItems.SHELL_MINECART)
                .withDefaults(ItemBasics::defaultLang)
                .withDeferredProvider(
                        ProviderTypes.ITEM_MODELS,
                        (entry, provider) -> provider.getBuilder(entry.getId().toString())
                                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", provider.mcLoc("item/minecart"))
                )
                .withDeferredProvider(
                        ProviderTypes.RECIPE,
                        (entry, provider) -> ShellItemRecipeBuilder.of(entry.get())
                                .withInput(Ingredient.of(Items.MINECART))
                                .save(provider, entry.getId())
                );

        dataRegistering.forEntry(TransportItems.PATTERNED_RAIL_LAYER)
                .withDefaults(ItemBasics::defaultLang)
                .withProvider(
                        ProviderTypes.TAGS,
                        (entry, provider) -> provider.tag(TransportItemTags.RAIL_PROVIDERS)
                                .with(entry.asItem())
                )
                .withDeferredProvider(
                        ProviderTypes.ITEM_MODELS,
                        (entry, provider) -> provider.getBuilder("item/patterned_rail_layer")
                                .parent(provider.getExistingFile(provider.mcLoc("item/generated")))
                                .customLoader(PatternedRailLayerCustomLoaderBuilder::new)
                                .withLayer(provider.mcLoc("block/smooth_stone"))
                )
                .withDeferredProvider(
                        ProviderTypes.RECIPE,
                        (entry, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, entry.get())
                                .pattern(" RI")
                                .pattern("RIR")
                                .pattern("IR ")
                                .define('R', Tags.Items.DYES_RED)
                                .define('I', Tags.Items.INGOTS_IRON)
                                .unlockedBy("has_item", provider.unlockedByTag(Tags.Items.INGOTS_IRON))
                                .save(provider)
                );
    }
}

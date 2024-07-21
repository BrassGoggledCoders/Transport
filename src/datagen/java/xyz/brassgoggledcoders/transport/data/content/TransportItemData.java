package xyz.brassgoggledcoders.transport.data.content;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.Slots;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderTypes;
import xyz.brassgoggledcoders.transport.content.TransportItems;
import xyz.brassgoggledcoders.transport.data.recipe.ShellItemRecipeBuilder;
import xyz.brassgoggledcoders.transport.data.util.ItemBasics;

import java.util.List;
import java.util.Optional;

public class TransportItemData {
    public static void generate(DataRegistering dataRegistering) {
        dataRegistering.forEntry(TransportItems.PATTERNED_RAIL_LAYER)
                .withDefaults(ItemBasics::defaultLang)
                .withDeferredProvider(
                        ProviderTypes.ITEM_MODELS,
                        (entry, provider) -> provider.basicItem(provider.mcLoc("item/minecart"))
                )
                .withDeferredProvider(
                        ProviderTypes.RECIPE,
                        (entry, provider) -> ShellItemRecipeBuilder.of(entry.get())
                                .withInput(Ingredient.of(Items.MINECART))
                                .save(provider, entry.getId())
                );

        dataRegistering.forEntry(TransportItems.PATTERNED_RAIL_LAYER)
                .withDefaults(ItemBasics::defaultItem)
                .withDeferredProvider(
                        ProviderTypes.RECIPE,
                        (entry, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, entry.get())
                                .pattern(" RI")
                                .pattern("RIR")
                                .pattern("IR ")
                                .define('R', Tags.Items.DYES_RED)
                                .define('I', Tags.Items.INGOTS_IRON)
                                .unlockedBy("has_item", CriteriaTriggers.INVENTORY_CHANGED
                                        .createCriterion(new InventoryChangeTrigger.TriggerInstance(
                                                Optional.empty(),
                                                Slots.ANY,
                                                List.of(ItemPredicate.Builder.item()
                                                        .of(Tags.Items.INGOTS_IRON)
                                                        .build()
                                                )
                                        )))
                                .save(provider)
                );
    }
}

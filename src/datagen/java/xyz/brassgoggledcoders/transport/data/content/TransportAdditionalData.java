package xyz.brassgoggledcoders.transport.data.content;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderTypes;
import xyz.brassgoggledcoders.shadyskies.dataregistering.recipe.DeferredRecipeProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.tags.DataRegisteringTagProvider;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.tag.TransportItemTags;
import xyz.brassgoggledcoders.transport.data.recipe.RailWorkerBenchRecipeBuilder;

public class TransportAdditionalData {

    public static void generate(DataRegistering dataRegistering) {
        dataRegistering.deferredWithProvider(ProviderTypes.RECIPE, TransportAdditionalData::vanillaRecipes);
        dataRegistering.doWithProvider(ProviderTypes.TAGS, TransportAdditionalData::vanillaItemTags);
    }

    public static void vanillaItemTags(DataRegisteringTagProvider tagsProvider) {
        tagsProvider.tag(TransportItemTags.RAILS_GOLD)
                .with(
                        Items.POWERED_RAIL
                );

        tagsProvider.tag(TransportItemTags.RAILS_IRON)
                .with(
                        Items.RAIL,
                        Items.DETECTOR_RAIL,
                        Items.ACTIVATOR_RAIL
                );
    }

    public static void vanillaRecipes(DeferredRecipeProvider recipeProvider) {
        RailWorkerBenchRecipeBuilder.of(Items.POWERED_RAIL)
                .withInput(Ingredient.of(TransportItemTags.RAILS_GOLD))
                .save(recipeProvider, Transport.rl("powered_rail_from_rails_gold"));

        RailWorkerBenchRecipeBuilder.of(Items.ACTIVATOR_RAIL)
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .save(recipeProvider, Transport.rl("activator_rail_from_rails_iron"));

        RailWorkerBenchRecipeBuilder.of(Items.RAIL)
                .withInput(Ingredient.of(TransportItemTags.RAILS_GOLD))
                .save(recipeProvider, Transport.rl("rail_from_rails_gold"));

        RailWorkerBenchRecipeBuilder.of(Items.RAIL)
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .save(recipeProvider, Transport.rl("rail_from_rails_iron"));

        RailWorkerBenchRecipeBuilder.of(Items.DETECTOR_RAIL)
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .save(recipeProvider, Transport.rl("detector_rail_from_rails_iron"));

        RailWorkerBenchRecipeBuilder.of(Items.MINECART)
                .withInput(Ingredient.of(Tags.Items.INGOTS_IRON), 3)
                .save(recipeProvider, Transport.rl("cheaper_minecart"));

        RailWorkerBenchRecipeBuilder.of(Items.RAIL, 32)
                .withInput(Ingredient.of(Tags.Items.INGOTS_IRON), 4)
                .withSecondaryInput(Ingredient.of(Tags.Items.RODS_WOODEN))
                .save(recipeProvider, Transport.rl("cheaper_rail"));

        RailWorkerBenchRecipeBuilder.of(Items.POWERED_RAIL, 12)
                .withInput(Ingredient.of(Tags.Items.INGOTS_GOLD), 4)
                .withSecondaryInput(Ingredient.of(Tags.Items.RODS_WOODEN))
                .save(recipeProvider, Transport.rl("cheaper_powered_rail"));
    }
}

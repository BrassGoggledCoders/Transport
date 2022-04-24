package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.RailWorkerBenchRecipeBuilder;

public class TransportAdditionalData {

    public static void vanillaItemTags(RegistrateTagsProvider<Item> tagsProvider) {
        tagsProvider.tag(TransportItemTags.RAILS_GOLD)
                .add(
                        Items.POWERED_RAIL
                );

        tagsProvider.tag(TransportItemTags.RAILS_IRON)
                .add(
                        Items.RAIL,
                        Items.DETECTOR_RAIL,
                        Items.ACTIVATOR_RAIL
                );
    }

    public static void vanillaRecipes(RegistrateRecipeProvider recipeProvider) {
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

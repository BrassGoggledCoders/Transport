package xyz.brassgoggledcoders.transport.content.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipeBuilder;

public class TransportAdditionalRecipes {
    public static void generate(RegistrateRecipeProvider recipeProvider) {
        RailWorkerBenchRecipeBuilder.create(ItemTags.RAILS, Items.RAIL)
                .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.RAIL))
                .build(recipeProvider, "minecraft_rail_from_rails");
    }
}

package xyz.brassgoggledcoders.transport.littlelogistics.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.tag.TransportItemTags;
import xyz.brassgoggledcoders.transport.data.recipe.RailWorkerBenchRecipeBuilder;
import xyz.brassgoggledcoders.transport.littlelogistics.TransportLL;

public class TransportLLData {

    public static void generateRecipes(RegistrateRecipeProvider provider) {
        RailWorkerBenchRecipeBuilder.of(llItem("switch_rail"))
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .save(provider, TransportLL.rl("switch_rail_from_rails_iron"));

        RailWorkerBenchRecipeBuilder.of(llItem("tee_junction_rail"))
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .save(provider, TransportLL.rl("tee_junction_rail_from_rails_iron"));


        RailWorkerBenchRecipeBuilder.of(llItem("junction_rail"))
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .save(provider, TransportLL.rl("junction_rail_from_rails_iron"));

        RailWorkerBenchRecipeBuilder.of(llItem("car_dock_rail"))
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .save(provider, TransportLL.rl("car_dock_rail_from_rails_iron"));

        RailWorkerBenchRecipeBuilder.of(llItem("locomotive_dock_rail"))
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .save(provider, TransportLL.rl("locomotive_docker_rail_from_rails_iron"));

        RailWorkerBenchRecipeBuilder.of(llItem("automatic_tee_junction_rail"))
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .withSecondaryInput(llItem("receiver_component"))
                .save(provider, TransportLL.rl("automatic_tee_junction_rail_from_rails_iron"));

        RailWorkerBenchRecipeBuilder.of(llItem("automatic_switch_rail"))
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .withSecondaryInput(llItem("receiver_component"))
                .save(provider, TransportLL.rl("automatic_switch_rail_from_rails_iron"));

        RailWorkerBenchRecipeBuilder.of(llItem("automatic_tee_junction_rail"))
                .withInput(Ingredient.of(TransportLLItemTags.RAILS_AUTOMATIC))
                .save(provider, TransportLL.rl("automatic_tee_junction_rail_from_rails_automatic"));

        RailWorkerBenchRecipeBuilder.of(llItem("automatic_switch_rail"))
                .withInput(Ingredient.of(TransportLLItemTags.RAILS_AUTOMATIC))
                .save(provider, TransportLL.rl("automatic_switch_rail_from_rails_automatic"));

        RailWorkerBenchRecipeBuilder.of(llItem("seater_car"))
                .withInput(Ingredient.of(Items.MINECART))
                .save(provider, TransportLL.rl("seater_car_from_minecart"));

        RailWorkerBenchRecipeBuilder.of(llItem("seater_car"))
                .withInput(Ingredient.of(Tags.Items.INGOTS_IRON), 2)
                .withSecondaryInput(Ingredient.of(ItemTags.PLANKS), 3)
                .save(provider, TransportLL.rl("seater_car"));

        RailWorkerBenchRecipeBuilder.of(llItem("chest_car"))
                .withInput(Ingredient.of(llItem("seater_car")))
                .withSecondaryInput(Ingredient.of(Items.CHEST))
                .save(provider, TransportLL.rl("chest_car"));

        RailWorkerBenchRecipeBuilder.of(llItem("fluid_car"))
                .withInput(Ingredient.of(llItem("seater_car")))
                .withSecondaryInput(Ingredient.of(Tags.Items.GLASS), 3)
                .save(provider, TransportLL.rl("fluid_car"));

        RailWorkerBenchRecipeBuilder.of(llItem("spring"), 6)
                .withInput(Items.STRING, 3)
                .withSecondaryInput(Tags.Items.NUGGETS_IRON, 3)
                .save(provider, TransportLL.rl("spring"));
    }

    public static void generateItemTags(RegistrateTagsProvider<Item> provider) {
        provider.tag(TransportItemTags.RAILS_IRON)
                .add(
                        llItem("switch_rail"),
                        llItem("tee_junction_rail"),
                        llItem("junction_rail"),
                        llItem("car_dock_rail"),
                        llItem("locomotive_docker_rail")
                );

        provider.tag(TransportLLItemTags.RAILS_AUTOMATIC)
                .add(
                        llItem("automatic_switch_rail"),
                        llItem("automatic_tee_junction_rail")
                );
    }

    private static Item llItem(String name) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation("littlelogistics", name));
    }
}

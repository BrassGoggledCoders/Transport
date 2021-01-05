package xyz.brassgoggledcoders.transport.content;

import com.google.common.collect.Lists;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.engine.EngineState;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipeBuilder;

import javax.annotation.Nullable;
import java.util.List;

public class TransportAdditionalData {
    private static final List<Pair<IItemProvider, ITag<Item>>> tagInfoList = Lists.newArrayList();

    public static void generateRecipes(RegistrateRecipeProvider recipeProvider) {
        List<Pair<IItemProvider, ITag<Item>>> itemTagList = getTagInfoList();

        railBenchRecipes(recipeProvider, itemTagList, TransportItemTags.RAILS_REGULAR, false);
        railBenchRecipes(recipeProvider, itemTagList, TransportItemTags.RAILS_POWERED, true);
        railBenchRecipes(recipeProvider, itemTagList, TransportItemTags.RAILS_STRUCTURE, true);

        RailWorkerBenchRecipeBuilder.create(Tags.Items.INGOTS_IRON, Items.CHAIN)
                .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.CHAIN))
                .build(recipeProvider, Transport.rl("minecraft_chain_from_forge_ingots_iron"));
    }

    public static void railBenchRecipes(RegistrateRecipeProvider recipeProvider, List<Pair<IItemProvider, ITag<Item>>> itemTagList,
                                        ITag.INamedTag<Item> itemTag, boolean includeBasicRail) {
        for (Pair<IItemProvider, ITag<Item>> rail : itemTagList) {
            if (rail.getRight() == itemTag) {
                RailWorkerBenchRecipeBuilder.create(itemTag, rail.getLeft())
                        .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.RAIL))
                        .build(recipeProvider, Transport.rl(fixRL(rail.getLeft().asItem().getRegistryName()) +
                                "_from_" + fixRL(itemTag.getName())));
            }
        }
        if (includeBasicRail) {
            RailWorkerBenchRecipeBuilder.create(itemTag, Items.RAIL)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.RAIL))
                    .build(recipeProvider, Transport.rl(fixRL(Items.RAIL.getRegistryName()) +
                            "_from_" + fixRL(itemTag.getName())));
        }
    }

    public static String fixRL(@Nullable ResourceLocation resourceLocation) {
        return resourceLocation != null ? resourceLocation.toString().replace(":", "_")
                .replace("/", "_") : "";
    }

    public static void generateBlockTags(RegistrateTagsProvider<Block> blockTagsProvider) {
        blockTagsProvider.getOrCreateBuilder(TransportBlockTags.RAILS_REGULAR)
                .add(Blocks.RAIL);

        blockTagsProvider.getOrCreateBuilder(TransportBlockTags.RAILS_POWERED)
                .add(Blocks.POWERED_RAIL, Blocks.DETECTOR_RAIL, Blocks.ACTIVATOR_RAIL);
    }

    public static void generateItemTags(RegistrateTagsProvider<Item> itemTagsProvider) {
        itemTagsProvider.getOrCreateBuilder(TransportItemTags.RAILS_REGULAR)
                .add(Items.RAIL);

        itemTagsProvider.getOrCreateBuilder(TransportItemTags.RAILS_POWERED)
                .add(Items.POWERED_RAIL, Items.DETECTOR_RAIL, Items.ACTIVATOR_RAIL);

        itemTagsProvider.getOrCreateBuilder(TransportItemTags.HULLS)
                .addTag(TransportItemTags.HULLS_BOAT)
                .addTag(TransportItemTags.HULLS_MINECART);

        itemTagsProvider.getOrCreateBuilder(TransportItemTags.RAILS)
                .addTag(TransportItemTags.RAILS_IRON)
                .addTag(TransportItemTags.RAILS_GOLD);
    }

    public static void generateLang(RegistrateLangProvider langProvider) {
        langProvider.add("screen.transport.jei.category.rail_workers_bench", "Rail Worker's Bench");

        langProvider.add(EngineState.FORWARD_3.getTranslationKey(), "Forward 3");
        langProvider.add(EngineState.FORWARD_2.getTranslationKey(), "Forward 2");
        langProvider.add(EngineState.FORWARD_1.getTranslationKey(), "Forward 1");
        langProvider.add(EngineState.NEUTRAL_0.getTranslationKey(), "Neutral");
        langProvider.add(EngineState.REVERSE_1.getTranslationKey(), "Reverse 1");
        langProvider.add(EngineState.REVERSE_2.getTranslationKey(), "Reverse 2");
        langProvider.add(EngineState.REVERSE_3.getTranslationKey(), "Reverse 3");
    }

    public static List<Pair<IItemProvider, ITag<Item>>> getTagInfoList() {
        if (tagInfoList.isEmpty()) {
            tagInfoList.add(Pair.of(Items.RAIL, TransportItemTags.RAILS_REGULAR));
            tagInfoList.add(Pair.of(TransportBlocks.SWITCH_RAIL.get(), TransportItemTags.RAILS_REGULAR));
            tagInfoList.add(Pair.of(TransportBlocks.WYE_SWITCH_RAIL.get(), TransportItemTags.RAILS_REGULAR));
            tagInfoList.add(Pair.of(TransportBlocks.BUMPER_RAIL.get(), TransportItemTags.RAILS_REGULAR));
            tagInfoList.add(Pair.of(TransportBlocks.DIAMOND_CROSSING_RAIL.get(), TransportItemTags.RAILS_REGULAR));

            tagInfoList.add(Pair.of(Items.POWERED_RAIL, TransportItemTags.RAILS_POWERED));
            tagInfoList.add(Pair.of(Items.DETECTOR_RAIL, TransportItemTags.RAILS_POWERED));
            tagInfoList.add(Pair.of(Items.ACTIVATOR_RAIL, TransportItemTags.RAILS_POWERED));
            tagInfoList.add(Pair.of(TransportBlocks.HOLDING_RAIL.get(), TransportItemTags.RAILS_POWERED));
            tagInfoList.add(Pair.of(TransportBlocks.TIMED_HOLDING_RAIL.get(), TransportItemTags.RAILS_POWERED));

            tagInfoList.add(Pair.of(TransportBlocks.ELEVATOR_SWITCH_RAIL.get(), TransportItemTags.RAILS_STRUCTURE));
            tagInfoList.add(Pair.of(TransportBlocks.SCAFFOLDING_RAIL.get(), TransportItemTags.RAILS_STRUCTURE));
        }
        return tagInfoList;
    }
}

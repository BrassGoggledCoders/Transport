package xyz.brassgoggledcoders.transport.content;

import com.google.common.collect.Lists;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipeBuilder;

import javax.annotation.Nullable;
import java.util.List;

public class TransportAdditionalData {
    private static final List<Pair<IItemProvider, ITag<Item>>> tagInfoList = Lists.newArrayList();

    public static void generateRecipes(RegistrateRecipeProvider recipeProvider) {
        List<Pair<IItemProvider, ITag<Item>>> itemTagList = getTagInfoList();

        railBenchRecipes(recipeProvider, itemTagList, TransportItemTags.REGULAR_RAILS, false);
        railBenchRecipes(recipeProvider, itemTagList, TransportItemTags.POWERED_RAILS, true);
        railBenchRecipes(recipeProvider, itemTagList, TransportItemTags.STRUCTURE_RAILS, true);
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
        blockTagsProvider.getOrCreateBuilder(TransportBlockTags.REGULAR_RAILS)
                .add(Blocks.RAIL);

        blockTagsProvider.getOrCreateBuilder(TransportBlockTags.POWERED_RAILS)
                .add(Blocks.POWERED_RAIL, Blocks.DETECTOR_RAIL, Blocks.ACTIVATOR_RAIL);
    }

    public static void generateItemTags(RegistrateTagsProvider<Item> itemTagsProvider) {
        itemTagsProvider.getOrCreateBuilder(TransportItemTags.REGULAR_RAILS)
                .add(Items.RAIL);

        itemTagsProvider.getOrCreateBuilder(TransportItemTags.POWERED_RAILS)
                .add(Items.POWERED_RAIL, Items.DETECTOR_RAIL, Items.ACTIVATOR_RAIL);
    }

    public static List<Pair<IItemProvider, ITag<Item>>> getTagInfoList() {
        if (tagInfoList.isEmpty()) {
            tagInfoList.add(Pair.of(Items.RAIL, TransportItemTags.REGULAR_RAILS));
            tagInfoList.add(Pair.of(TransportBlocks.SWITCH_RAIL.get(), TransportItemTags.REGULAR_RAILS));
            tagInfoList.add(Pair.of(TransportBlocks.WYE_SWITCH_RAIL.get(), TransportItemTags.REGULAR_RAILS));
            tagInfoList.add(Pair.of(TransportBlocks.BUMPER_RAIL.get(), TransportItemTags.REGULAR_RAILS));
            tagInfoList.add(Pair.of(TransportBlocks.DIAMOND_CROSSING_RAIL.get(), TransportItemTags.REGULAR_RAILS));

            tagInfoList.add(Pair.of(Items.POWERED_RAIL, TransportItemTags.POWERED_RAILS));
            tagInfoList.add(Pair.of(Items.DETECTOR_RAIL, TransportItemTags.POWERED_RAILS));
            tagInfoList.add(Pair.of(Items.ACTIVATOR_RAIL, TransportItemTags.POWERED_RAILS));
            tagInfoList.add(Pair.of(TransportBlocks.HOLDING_RAIL.get(), TransportItemTags.POWERED_RAILS));
            tagInfoList.add(Pair.of(TransportBlocks.TIMED_HOLDING_RAIL.get(), TransportItemTags.POWERED_RAILS));

            tagInfoList.add(Pair.of(TransportBlocks.ELEVATOR_SWITCH_RAIL.get(), TransportItemTags.STRUCTURE_RAILS));
            tagInfoList.add(Pair.of(TransportBlocks.SCAFFOLDING_RAIL.get(), TransportItemTags.STRUCTURE_RAILS));
        }
        return tagInfoList;
    }
}

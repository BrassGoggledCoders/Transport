package xyz.brassgoggledcoders.transport.data.content;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.item.DeferredItemModelProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderTypes;
import xyz.brassgoggledcoders.shadyskies.dataregistering.registering.DataRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.dataregistering.tags.DataRegisteringTagProvider;
import xyz.brassgoggledcoders.shadyskies.registering.block.BlockEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.tag.TransportItemTags;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.data.recipe.RailWorkerBenchRecipeBuilder;
import xyz.brassgoggledcoders.transport.data.util.BlockModelHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TransportBlockData {
    public static void generate(DataRegistering dataRegistering) {
        dataRegistering.forEntry(TransportBlocks.ITEM_DUMP_RAIL)
                .withDefaults(TransportBlockData::defaultStraightRail)
                .withDefaults(TransportBlockData::defaultIronRail);

        dataRegistering.forEntry(TransportBlocks.FLUID_DUMP_RAIL)
                .withDefaults(TransportBlockData::defaultStraightRail)
                .withDefaults(TransportBlockData::defaultIronRail);

        dataRegistering.forEntry(TransportBlocks.ENERGY_DUMP_RAIL)
                .withDefaults(TransportBlockData::defaultStraightRail)
                .withDefaults(TransportBlockData::defaultIronRail);

        dataRegistering.forEntry(TransportBlocks.ITEM_LOADING_RAIL)
                .withDefaults(TransportBlockData::defaultStraightRail)
                .withDefaults(TransportBlockData::defaultIronRail);

        dataRegistering.forEntry(TransportBlocks.FLUID_LOADING_RAIL)
                .withDefaults(TransportBlockData::defaultStraightRail)
                .withDefaults(TransportBlockData::defaultIronRail);

        dataRegistering.forEntry(TransportBlocks.ENERGY_LOADING_RAIL)
                .withDefaults(TransportBlockData::defaultStraightRail)
                .withDefaults(TransportBlockData::defaultIronRail);

        dataRegistering.forEntry(TransportBlocks.ITEM_UNLOADING_RAIL)
                .withDefaults(TransportBlockData::defaultStraightRail)
                .withDefaults(TransportBlockData::defaultIronRail);

        dataRegistering.forEntry(TransportBlocks.FLUID_UNLOADING_RAIL)
                .withDefaults(TransportBlockData::defaultStraightRail)
                .withDefaults(TransportBlockData::defaultIronRail);

        dataRegistering.forEntry(TransportBlocks.ENERGY_UNLOADING_RAIL)
                .withDefaults(TransportBlockData::defaultStraightRail)
                .withDefaults(TransportBlockData::defaultIronRail);

        dataRegistering.forEntry(TransportBlocks.WAXED_OXIDIZED_COPPER_RAIL)
                .withDefaults(TransportBlockData.defaultNamedRail("oxidized_copper_rail"))
                .withDefaults(TransportBlockData::defaultCopperRail);

        dataRegistering.forEntry(TransportBlocks.OXIDIZED_COPPER_RAIL)
                .withDefaults(TransportBlockData::defaultRail)
                .withDefaults(TransportBlockData::defaultCopperRail);

        dataRegistering.forEntry(TransportBlocks.WAXED_WEATHERED_COPPER_RAIL)
                .withDefaults(TransportBlockData.defaultNamedRail("weathered_copper_rail"))
                .withDefaults(TransportBlockData::defaultCopperRail);

        dataRegistering.forEntry(TransportBlocks.WEATHERED_COPPER_RAIL)
                .withDefaults(TransportBlockData::defaultRail)
                .withDefaults(TransportBlockData::defaultCopperRail);

        dataRegistering.forEntry(TransportBlocks.WAXED_EXPOSED_COPPER_RAIL)
                .withDefaults(TransportBlockData.defaultNamedRail("exposed_copper_rail"))
                .withDefaults(TransportBlockData::defaultCopperRail);

        dataRegistering.forEntry(TransportBlocks.EXPOSED_COPPER_RAIL)
                .withDefaults(TransportBlockData::defaultRail)
                .withDefaults(TransportBlockData::defaultCopperRail);

        dataRegistering.forEntry(TransportBlocks.WAXED_COPPER_RAIL)
                .withDefaults(TransportBlockData.defaultNamedRail("copper_rail"))
                .withDefaults(TransportBlockData::defaultCopperRail);

        dataRegistering.forEntry(TransportBlocks.COPPER_RAIL)
                .withDefaults(TransportBlockData::defaultRail)
                .withDefaults(TransportBlockData::defaultCopperRail)
                .withDeferredProvider(ProviderTypes.RECIPE, (entry, provider) -> {
                    RailWorkerBenchRecipeBuilder.of(entry)
                            .withInput(Ingredient.of(TransportItemTags.RAILS_COPPER))
                            .save(provider, Transport.rl("copper_rail_from_rails_copper"));

                    RailWorkerBenchRecipeBuilder.of(entry, 32)
                            .withInput(Ingredient.of(Tags.Items.INGOTS_COPPER), 4)
                            .withSecondaryInput(Ingredient.of(Tags.Items.RODS_WOODEN))
                            .save(provider, Transport.rl("cheaper_copper_rail"));

                    ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, entry, 16)
                            .pattern("C C")
                            .pattern("CSC")
                            .pattern("C C")
                            .define('C', Tags.Items.INGOTS_COPPER)
                            .define('S', Tags.Items.RODS_WOODEN)
                            .unlockedBy("has_item", CriteriaTriggers.INVENTORY_CHANGED
                                    .createCriterion(new InventoryChangeTrigger.TriggerInstance(
                                            Optional.empty(),
                                            InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                                            List.of(ItemPredicate.Builder.item()
                                                    .of(Tags.Items.INGOTS_COPPER)
                                                    .build()
                                            )
                                    ))
                            )
                            .save(provider, Transport.rl("copper_rail"));
                });
    }

    private static <R extends BaseRailBlock> Consumer<DataRegisteringEntry<BlockEntry<R>, R, Block>> defaultNamedRail(
            String realName
    ) {
        return blockEntry -> {
            blockEntry.withDeferredProvider(
                    ProviderTypes.BLOCKSTATE,
                    (entry, provider) -> BlockModelHelper.regularRail(realName, entry.get(), provider)
            );
            blockEntry.withDeferredProvider(
                    ProviderTypes.ITEM_MODELS,
                    (entry, provider) -> provider.getBuilder(entry.getId().toString())
                            .parent(new ModelFile.UncheckedModelFile("item/generated"))
                            .texture("layer0", new ResourceLocation(
                                    entry.getId().getNamespace(),
                                    "block/rail/" + realName
                            ))
            );
        };
    }

    private static <R extends BaseRailBlock> void defaultIronRail(
            DataRegisteringEntry<BlockEntry<R>, R, Block> blockEntry
    ) {
        blockEntry.withProvider(ProviderTypes.TAGS, (entry, provider) -> provider.tag(TransportItemTags.RAILS_IRON)
                .with(entry.asItem())
        );
        blockEntry.withDeferredProvider(ProviderTypes.RECIPE, (entry, provider) -> RailWorkerBenchRecipeBuilder.of(entry)
                .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                .save(provider)
        );
    }

    private static <R extends BaseRailBlock> void defaultCopperRail(
            DataRegisteringEntry<BlockEntry<R>, R, Block> blockEntry
    ) {
        blockEntry.withProvider(ProviderTypes.TAGS, (entry, provider) -> provider.tag(TransportItemTags.RAILS_COPPER)
                .with(entry.asItem())
        );
    }

    private static <R extends BaseRailBlock> void defaultBaseRail(
            DataRegisteringEntry<BlockEntry<R>, R, Block> entry
    ) {
        entry.withProvider(ProviderTypes.TAGS, TransportBlockData::railTag);
        entry.withDeferredProvider(ProviderTypes.ITEM_MODELS, TransportBlockData::railItemModel);
    }

    private static <R extends BaseRailBlock> void defaultRail(
            DataRegisteringEntry<BlockEntry<R>, R, Block> blockEntry
    ) {
        TransportBlockData.defaultBaseRail(blockEntry);
        blockEntry.withDeferredProvider(ProviderTypes.BLOCKSTATE, BlockModelHelper::regularRail);
    }

    private static <R extends BaseRailBlock> void defaultStraightRail(
            DataRegistering dataRegistering,
            DataRegisteringEntry<BlockEntry<R>, R, Block> blockEntry
    ) {
        TransportBlockData.defaultBaseRail(blockEntry);
        blockEntry.withDeferredProvider(ProviderTypes.BLOCKSTATE, BlockModelHelper::straightPoweredRailBlockState);
    }

    private static <R extends BaseRailBlock> void railTag(BlockEntry<R> blockEntry, DataRegisteringTagProvider tagProvider) {
        tagProvider.tag(BlockTags.RAILS)
                .with(blockEntry.get());

        tagProvider.tag(ItemTags.RAILS)
                .with(blockEntry.asItem());
    }

    private static <R extends BaseRailBlock> void railItemModel(BlockEntry<R> blockEntry, DeferredItemModelProvider itemModelProvider) {
        itemModelProvider.getBuilder(blockEntry.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(blockEntry.getId().getNamespace(), "block/rail/" + blockEntry.getName()));
    }
}

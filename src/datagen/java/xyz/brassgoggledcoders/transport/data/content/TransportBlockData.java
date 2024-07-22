package xyz.brassgoggledcoders.transport.data.content;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.basic.BlockBasics;
import xyz.brassgoggledcoders.shadyskies.dataregistering.item.DeferredItemModelProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderTypes;
import xyz.brassgoggledcoders.shadyskies.dataregistering.registering.DataRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.dataregistering.tags.DataRegisteringTagProvider;
import xyz.brassgoggledcoders.shadyskies.registering.block.BlockEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.tag.TransportItemTags;
import xyz.brassgoggledcoders.transport.block.storage.CapabilityStorageBlock;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.data.recipe.RailWorkerBenchRecipeBuilder;
import xyz.brassgoggledcoders.transport.data.util.BlockModelHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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
                            .unlockedBy("has_item", unlockedByTag(Tags.Items.INGOTS_COPPER))
                            .save(provider, Transport.rl("copper_rail"));
                });

        dataRegistering.forEntry(TransportBlocks.ONE_WAY_BOOSTER_RAIL)
                .withDefaults(BlockBasics::defaultLoot)
                .withDefaults(TransportBlockData::defaultBaseRail)
                .withDefaults(TransportBlockData::defaultGoldRail)
                .withDeferredProvider(
                        ProviderTypes.BLOCKSTATE,
                        BlockModelHelper::straightPoweredInvertedRailBlockState
                );

        dataRegistering.forEntry(TransportBlocks.SWITCH_RAIL)
                .withDefaults(TransportBlockData::defaultIronRail)
                .withDefaults(BlockBasics::defaultLoot)
                .withProvider(ProviderTypes.TAGS, TransportBlockData::railTag)
                .withDeferredProvider(
                        ProviderTypes.BLOCKSTATE,
                        BlockModelHelper::switchRail
                )
                .withDeferredProvider(
                        ProviderTypes.ITEM_MODELS,
                        (entry, provider) -> railItemModel(entry, provider, "switch_rail_straight_right")
                );

        dataRegistering.forEntry(TransportBlocks.WYE_SWITCH_RAIL)
                .withDefaults(TransportBlockData::defaultBaseRail)
                .withDefaults(TransportBlockData::defaultIronRail)
                .withDeferredProvider(
                        ProviderTypes.BLOCKSTATE,
                        BlockModelHelper::wyeSwitchRail
                );

        dataRegistering.forEntry(TransportBlocks.DIAMOND_CROSSING_RAIL)
                .withDefaults(TransportBlockData::defaultBaseRail)
                .withDefaults(TransportBlockData::defaultIronRail)
                .withDeferredProvider(
                        ProviderTypes.BLOCKSTATE,
                        (entry, provider) -> provider.simpleBlock(
                                entry.get(),
                                provider.models()
                                        .getBuilder("block/" + entry.getName())
                                        .parent(provider.models()
                                                .getExistingFile(provider.mcLoc("block/rail_flat"))
                                        )
                                        .texture("rail", provider.modLoc("block/rail/" + entry.getName()))
                        )
                );

        dataRegistering.forEntry(TransportBlocks.BUMPER_RAIL)
                .withDefaults(TransportBlockData::defaultBaseRail)
                .withDefaults(TransportBlockData::defaultIronRail)
                .withDeferredProvider(
                        ProviderTypes.BLOCKSTATE,
                        (entry, provider) -> BlockModelHelper.straightInvertedFlatRailBlockState(entry, provider, provider.modLoc("block/buffer_rail"))
                );

        dataRegistering.forEntry(TransportBlocks.INVERTED_POWERED_RAIL)
                .withDefaults(TransportBlockData::defaultGoldRail)
                .withProvider(ProviderTypes.TAGS, TransportBlockData::railTag)
                .withDeferredProvider(
                        ProviderTypes.BLOCKSTATE,
                        (entry, provider) -> BlockModelHelper.straightPoweredRailBlockState(
                                entry,
                                provider,
                                provider.mcLoc("block/powered_rail"),
                                provider.mcLoc("block/powered_rail_on")
                        )
                )
                .withDeferredProvider(
                        ProviderTypes.ITEM_MODELS,
                        (entry, provider) -> provider.getBuilder(entry.getId().toString())
                                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", provider.mcLoc("block/powered_rail_on"))
                );

        dataRegistering.forEntry(TransportBlocks.FLUID_STORAGE)
                .withDefaults(entry -> defaultCapabilityStorageBlock(
                        entry,
                        Ingredient.of(Items.BUCKET),
                        builder -> builder.of(Items.BUCKET)
                ));

        dataRegistering.forEntry(TransportBlocks.ENERGY_STORAGE)
                .withDefaults(entry -> defaultCapabilityStorageBlock(
                        entry,
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                        builder -> builder.of(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                ));

        dataRegistering.forEntry(TransportBlocks.RAIL_WORKER_BENCH)
                .withDefaults(BlockBasics::defaultLoot)
                .withDefaults(BlockBasics::defaultLang)
                .withDeferredProvider(
                        ProviderTypes.BLOCKSTATE,
                        (entry, provider) -> provider.simpleBlock(
                                entry.get(),
                                provider.models()
                                        .getExistingFile(provider.modLoc("block/rail_worker_bench"))
                        )
                )
                .withDeferredProvider(
                        ProviderTypes.RECIPE,
                        (entry, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, entry.get())
                                .pattern("RRR")
                                .pattern("SSS")
                                .define('R', ItemTags.RAILS)
                                .define('S', Tags.Items.STONE)
                                .unlockedBy("has_item", unlockedByTag(ItemTags.RAILS))
                                .save(provider)
                );
    }

    private static <B extends CapabilityStorageBlock<?>> void defaultCapabilityStorageBlock(
            DataRegisteringEntry<BlockEntry<B>, B, Block> blockEntry,
            Ingredient ingredient,
            Function<ItemPredicate.Builder, ItemPredicate.Builder> setPredicate
    ) {
        blockEntry.withDefaults(BlockBasics::defaultLoot)
                .withDefaults(BlockBasics::defaultLang)
                .withDeferredProvider(
                        ProviderTypes.BLOCKSTATE,
                        BlockModelHelper::storageBlock
                )
                .withDeferredProvider(
                        ProviderTypes.RECIPE,
                        (entry, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, entry.get())
                                .pattern("III")
                                .pattern("GBG")
                                .pattern("III")
                                .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                                .define('G', Ingredient.of(Tags.Items.GLASS))
                                .define('B', ingredient)
                                .unlockedBy("has_item", CriteriaTriggers.INVENTORY_CHANGED
                                        .createCriterion(new InventoryChangeTrigger.TriggerInstance(
                                                Optional.empty(),
                                                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                                                List.of(setPredicate.apply(ItemPredicate.Builder.item())
                                                        .build()
                                                )
                                        ))
                                )
                                .save(provider)
                );
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

    private static <R extends BaseRailBlock> void defaultGoldRail(
            DataRegisteringEntry<BlockEntry<R>, R, Block> blockEntry
    ) {
        blockEntry.withProvider(ProviderTypes.TAGS, (entry, provider) -> provider.tag(TransportItemTags.RAILS_GOLD)
                .with(entry.asItem())
        );
        blockEntry.withDeferredProvider(ProviderTypes.RECIPE, (entry, provider) -> RailWorkerBenchRecipeBuilder.of(entry)
                .withInput(Ingredient.of(TransportItemTags.RAILS_GOLD))
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
        entry.withDefaults(BlockBasics::defaultLoot);
    }

    private static <R extends BaseRailBlock> void defaultRail(
            DataRegisteringEntry<BlockEntry<R>, R, Block> blockEntry
    ) {
        TransportBlockData.defaultBaseRail(blockEntry);
        blockEntry.withDeferredProvider(ProviderTypes.BLOCKSTATE, BlockModelHelper::regularRail);
    }

    private static <R extends BaseRailBlock> void defaultStraightRail(
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

    @SuppressWarnings("SameParameterValue")
    private static <R extends BaseRailBlock> void railItemModel(BlockEntry<R> blockEntry, DeferredItemModelProvider itemModelProvider, String name) {
        itemModelProvider.getBuilder(blockEntry.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(blockEntry.getId().getNamespace(), "block/rail/" + name));
    }

    @SuppressWarnings("unused")
    private static Criterion<?> unlockedByItem(ItemLike item) {
        return CriteriaTriggers.INVENTORY_CHANGED
                .createCriterion(new InventoryChangeTrigger.TriggerInstance(
                        Optional.empty(),
                        InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                        List.of(ItemPredicate.Builder.item()
                                .of(item)
                                .build()
                        )
                ));
    }

    @SuppressWarnings("SameParameterValue")
    private static Criterion<?> unlockedByTag(TagKey<Item> item) {
        return CriteriaTriggers.INVENTORY_CHANGED
                .createCriterion(new InventoryChangeTrigger.TriggerInstance(
                        Optional.empty(),
                        InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                        List.of(ItemPredicate.Builder.item()
                                .of(item)
                                .build()
                        )
                ));
    }
}

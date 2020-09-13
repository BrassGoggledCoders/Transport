package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.BuoyBlock;
import xyz.brassgoggledcoders.transport.block.DockBlock;
import xyz.brassgoggledcoders.transport.block.ModuleConfiguratorBlock;
import xyz.brassgoggledcoders.transport.block.ScaffoldingSlabBlock;
import xyz.brassgoggledcoders.transport.block.jobsite.RailWorkerBenchBlock;
import xyz.brassgoggledcoders.transport.block.loader.EnergyLoaderBlock;
import xyz.brassgoggledcoders.transport.block.loader.FluidLoaderBlock;
import xyz.brassgoggledcoders.transport.block.loader.ItemLoaderBlock;
import xyz.brassgoggledcoders.transport.block.rail.*;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchSupportBlock;
import xyz.brassgoggledcoders.transport.block.rail.turnout.SwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.turnout.WyeSwitchRailBlock;
import xyz.brassgoggledcoders.transport.container.jobsite.RailWorkerBenchContainer;
import xyz.brassgoggledcoders.transport.item.BuoyBlockItem;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrate;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrateBlockLootTables;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrateModels;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrateRecipes;
import xyz.brassgoggledcoders.transport.screen.jobsite.RailWorkerBenchScreen;
import xyz.brassgoggledcoders.transport.tileentity.ModuleConfiguratorTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.EnergyLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.FluidLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.ItemLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.rail.SwitchRailTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.rail.TimedHoldingRailTileEntity;

public class TransportBlocks {
    //region Rails
    public static final BlockEntry<HoldingRailBlock> HOLDING_RAIL =
            createRail("holding_rail", "Holding Rail", HoldingRailBlock::new)
                    .recipe(((context, recipeProvider) ->
                            ShapelessRecipeBuilder.shapelessRecipe(context.get(), 2)
                                    .addIngredient(Items.RAIL)
                                    .addIngredient(Items.POWERED_RAIL)
                                    .addCriterion("has_rail", RegistrateRecipeProvider.hasItem(ItemTags.RAILS))
                                    .build(recipeProvider)))
                    .tag(TransportItemTags.RAILS_POWERED)
                    .build()
                    .tag(TransportBlockTags.RAILS_POWERED)
                    .blockstate((context, provider) -> {
                    })
                    .register();


    public static final BlockEntry<DiamondCrossingRailBlock> DIAMOND_CROSSING_RAIL =
            createRail("diamond_crossing_rail", "Diamond Crossing Rail", DiamondCrossingRailBlock::new)
                    .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get(), 5)
                            .patternLine(" R ")
                            .patternLine("RRR")
                            .patternLine(" R ")
                            .key('R', Ingredient.fromItems(Items.RAIL))
                            .addCriterion("has_rail", RegistrateRecipeProvider.hasItem(ItemTags.RAILS))
                            .build(provider))
                    .tag(TransportItemTags.RAILS_REGULAR)
                    .build()
                    .tag(TransportBlockTags.RAILS_REGULAR)
                    .blockstate((context, provider) -> {
                    })
                    .register();

    public static final BlockEntry<ElevatorSwitchRailBlock> ELEVATOR_SWITCH_RAIL =
            createRail("elevator_switch_rail", "Elevator Switch Rail", ElevatorSwitchRailBlock::new)
                    .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                            .patternLine("R")
                            .patternLine("P")
                            .patternLine("S")
                            .key('R', Ingredient.fromItems(Items.RAIL))
                            .key('P', Tags.Items.DUSTS_REDSTONE)
                            .key('S', Ingredient.fromItems(Items.SCAFFOLDING))
                            .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.SCAFFOLDING))
                            .build(provider)
                    )
                    .model((context, provider) -> provider.generated(context, provider.mcLoc("block/scaffolding_top"),
                            provider.mcLoc("block/rail")))
                    .tag(TransportItemTags.RAILS_STRUCTURE)
                    .build()
                    .tag(TransportBlockTags.RAILS_STRUCTURE)
                    .blockstate((context, provider) -> {
                    })
                    .register();

    public static final BlockEntry<ScaffoldingRailBlock> SCAFFOLDING_RAIL =
            createRail("scaffolding_rail", "Scaffolding Rail", ScaffoldingRailBlock::new)
                    .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get(), 3)
                            .patternLine("RRR")
                            .patternLine("SSS")
                            .key('R', Ingredient.fromItems(Items.RAIL))
                            .key('S', Ingredient.fromItems(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.get()))
                            .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.SCAFFOLDING))
                            .build(provider)
                    )
                    .model((context, provider) -> provider.generated(context, provider.mcLoc("block/scaffolding_top"),
                            provider.mcLoc("block/rail")))
                    .tag(TransportItemTags.RAILS_STRUCTURE)
                    .build()
                    .tag(TransportBlockTags.RAILS_STRUCTURE)
                    .blockstate((context, provider) -> {
                    })
                    .register();
    public static final BlockEntry<SwitchRailBlock> SWITCH_RAIL =
            createRail("switch_rail", "Switch Rail", SwitchRailBlock::new)
                    .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get(), 4)
                            .patternLine("R ")
                            .patternLine("RR")
                            .patternLine("R ")
                            .key('R', Items.RAIL)
                            .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.RAIL))
                            .build(provider)
                    )
                    .model(TransportRegistrateModels.railItem("switch_rail_straight_right"))
                    .tag(TransportItemTags.RAILS_REGULAR)
                    .build()
                    .tag(TransportBlockTags.RAILS_REGULAR)
                    .blockstate((context, provider) -> {
                    })
                    .register();

    public static final BlockEntry<WyeSwitchRailBlock> WYE_SWITCH_RAIL =
            createRail("wye_switch_rail", "Wye Switch Rail", WyeSwitchRailBlock::new)
                    .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get(), 4)
                            .patternLine("RRR")
                            .patternLine(" R ")
                            .key('R', Items.RAIL)
                            .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.RAIL))
                            .build(provider)
                    )
                    .tag(TransportItemTags.RAILS_REGULAR)
                    .build()
                    .tag(TransportBlockTags.RAILS_REGULAR)
                    .blockstate((context, provider) -> {
                    })
                    .register();

    public static final RegistryEntry<TileEntityType<SwitchRailTileEntity>> SWITCH_RAIL_TILE_ENTITY = Transport.getRegistrate()
            .tileEntity(SwitchRailTileEntity::new)
            .validBlocks(SWITCH_RAIL, WYE_SWITCH_RAIL)
            .register();

    public static final BlockEntry<BumperRailBlock> BUMPER_RAIL =
            createRail("bumper_rail", "Bumper Rail", BumperRailBlock::new)
                    .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get(), 3)
                            .patternLine("WRW")
                            .patternLine("I I")
                            .patternLine("TTT")
                            .key('W', Tags.Items.DYES_WHITE)
                            .key('R', Tags.Items.DYES_RED)
                            .key('I', Tags.Items.INGOTS_IRON)
                            .key('T', Items.RAIL)
                            .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.RAIL))
                            .build(provider)
                    )
                    .model((context, provider) -> provider.blockItem(context))
                    .tag(TransportItemTags.RAILS_REGULAR)
                    .build()
                    .tag(TransportBlockTags.RAILS_REGULAR)
                    .blockstate((context, provider) -> {
                    })
                    .register();

    public static final BlockEntry<TimedHoldingRailBlock> TIMED_HOLDING_RAIL =
            createRail("timed_holding_rail", "Timed Holding Rail", TimedHoldingRailBlock::new)
                    .recipe((context, provider) -> ShapelessRecipeBuilder.shapelessRecipe(context.get(), 2)
                            .addIngredient(Items.RAIL)
                            .addIngredient(Items.REPEATER)
                            .addCriterion("has_rail", RegistrateRecipeProvider.hasItem(ItemTags.RAILS))
                            .build(provider))
                    .tag(TransportItemTags.RAILS_POWERED)
                    .build()
                    .tag(TransportBlockTags.RAILS_POWERED)
                    .blockstate((context, provider) -> {
                    })
                    .tileEntity(TimedHoldingRailTileEntity::new)
                    .build()
                    .register();
    //endregion

    //region Loaders
    public static final BlockEntry<ItemLoaderBlock> ITEM_LOADER = Transport.getRegistrate()
            .object("item_loader")
            .block(Material.IRON, ItemLoaderBlock::new)
            .lang("Item Loader")
            .properties(loaderProperties())
            .blockstate((context, provider) -> {
            })
            .loot(TransportRegistrateBlockLootTables::registerLoader)
            .item()
            .model((context, provider) -> {
            })
            .recipe(TransportRegistrateRecipes.createLoader(Tags.Items.CHESTS))
            .build()
            .tileEntity(ItemLoaderTileEntity::new)
            .build()
            .register();

    public static final RegistryEntry<TileEntityType<ItemLoaderTileEntity>> ITEM_LOADER_TILE_ENTITY =
            ITEM_LOADER.getSibling(ForgeRegistries.TILE_ENTITIES);

    public static final BlockEntry<FluidLoaderBlock> FLUID_LOADER = Transport.getRegistrate()
            .object("fluid_loader")
            .block(Material.IRON, FluidLoaderBlock::new)
            .lang("Fluid Loader")
            .properties(loaderProperties())
            .blockstate((context, provider) -> {
            })
            .loot(TransportRegistrateBlockLootTables::registerLoader)
            .item()
            .model((context, provider) -> {
            })
            .recipe(TransportRegistrateRecipes.createLoader(Items.BUCKET))
            .build()
            .tileEntity(FluidLoaderTileEntity::new)
            .build()
            .register();

    public static final RegistryEntry<TileEntityType<FluidLoaderTileEntity>> FLUID_LOADER_TILE_ENTITY =
            FLUID_LOADER.getSibling(ForgeRegistries.TILE_ENTITIES);

    public static final BlockEntry<EnergyLoaderBlock> ENERGY_LOADER = Transport.getRegistrate()
            .object("energy_loader")
            .block(Material.IRON, EnergyLoaderBlock::new)
            .lang("Energy Loader")
            .properties(loaderProperties())
            .loot(TransportRegistrateBlockLootTables::registerLoader)
            .blockstate((context, provider) -> {
            })
            .item()
            .model((context, provider) -> {
            })
            .recipe(TransportRegistrateRecipes.createLoader(Tags.Items.DUSTS_REDSTONE))
            .build()
            .tileEntity(EnergyLoaderTileEntity::new)
            .build()
            .register();

    public static final RegistryEntry<TileEntityType<EnergyLoaderTileEntity>> ENERGY_LOADER_TILE_ENTITY =
            ENERGY_LOADER.getSibling(ForgeRegistries.TILE_ENTITIES);

    private static NonNullUnaryOperator<AbstractBlock.Properties> loaderProperties() {
        return properties -> properties.hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.METAL);
    }
    //endregion

    //region Assorted
    public static final BlockEntry<ScaffoldingSlabBlock> SCAFFOLDING_SLAB_BLOCK = Transport.getRegistrate()
            .object("scaffolding_slab")
            .block(ScaffoldingSlabBlock::new)
            .initialProperties(Material.MISCELLANEOUS, MaterialColor.SAND)
            .properties(properties -> properties.doesNotBlockMovement()
                    .sound(SoundType.SCAFFOLDING)
                    .variableOpacity()
            )
            .blockstate((context, provider) -> {
            })
            .lang("Scaffolding Slab")
            .loot((blockLootTables, scaffoldingSlabBlock) -> blockLootTables.registerLootTable(scaffoldingSlabBlock,
                    LootTable.builder()
                            .addLootPool(LootPool.builder()
                                    .rolls(ConstantRange.of(1))
                                    .acceptCondition(BlockStateProperty.builder(scaffoldingSlabBlock)
                                            .fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withBoolProp(ScaffoldingSlabBlock.RAILED, false)))
                                    .addEntry(TransportRegistrateBlockLootTables.withExplosionDecay(
                                            scaffoldingSlabBlock, ItemLootEntry.builder(scaffoldingSlabBlock)
                                                    .acceptFunction(SetCount.builder(ConstantRange.of(2))
                                                            .acceptCondition(BlockStateProperty.builder(scaffoldingSlabBlock)
                                                                    .fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                                            .withProp(SlabBlock.TYPE, SlabType.DOUBLE)
                                                                    )
                                                            )
                                                    ))
                                    )
                            )
            ))
            .item()
            .model((context, provider) -> provider.blockItem(context, "_bottom"))
            .recipe(TransportRegistrateRecipes.slab(Items.SCAFFOLDING))
            .build()
            .register();

    public static final BlockEntry<ElevatorSwitchSupportBlock> ELEVATOR_SWITCH_SUPPORT = Transport.getRegistrate()
            .object("elevator_switch_support")
            .block(ElevatorSwitchSupportBlock::new)
            .initialProperties(Material.MISCELLANEOUS, MaterialColor.SAND)
            .properties(properties -> properties.doesNotBlockMovement()
                    .sound(SoundType.SCAFFOLDING)
                    .tickRandomly()
                    .variableOpacity()
            )
            .blockstate((context, provider) -> {
            })
            .lang("Elevator Switch Support")
            .loot(((blockLootTables, block) -> blockLootTables.registerLootTable(block, LootTable.builder())))
            .register();

    public static final BlockEntry<ModuleConfiguratorBlock> MODULE_CONFIGURATOR = Transport.getRegistrate()
            .object("module_configurator")
            .block(ModuleConfiguratorBlock::new)
            .initialProperties(Material.IRON, MaterialColor.IRON)
            .properties(properties -> properties.harvestTool(ToolType.PICKAXE)
                    .notSolid()
            )
            .blockstate((context, provider) -> {
            })
            .lang("Module Configurator")
            .loot((blockLootTables, moduleConfiguratorBlock) -> blockLootTables.registerLootTable(moduleConfiguratorBlock,
                    LootTable.builder()
                            .addLootPool(LootPool.builder()
                                    .rolls(ConstantRange.of(1))
                                    .acceptCondition(SurvivesExplosion.builder())
                                    .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                            .replaceOperation("modularInventory", "BlockEntityTag.modularInventory")
                                    )
                                    .addEntry(ItemLootEntry.builder(moduleConfiguratorBlock))
                            )
            ))
            .item()
            .recipe(TransportRegistrateRecipes.dualSlab(Tags.Items.INGOTS_IRON, Ingredient.fromItems(Items.CRAFTING_TABLE)))
            .build()
            .tileEntity(ModuleConfiguratorTileEntity::new)
            .build()
            .register();

    public static final RegistryEntry<TileEntityType<ModuleConfiguratorTileEntity>> MODULE_CONFIGURATOR_TILE_ENTITY =
            MODULE_CONFIGURATOR.getSibling(ForgeRegistries.TILE_ENTITIES);

    public static final BlockEntry<BuoyBlock> BUOY = Transport.getRegistrate()
            .object("buoy")
            .block(Material.IRON, BuoyBlock::new)
            .lang("Buoy")
            .properties(AbstractBlock.Properties::doesNotBlockMovement)
            .properties(properties -> properties.setLightLevel(BuoyBlock::getLightLevel))
            .blockstate((context, provider) -> {
            })
            .loot((blockLootTables, buoyBlock) -> blockLootTables.registerLootTable(buoyBlock, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .acceptCondition(BlockStateProperty.builder(buoyBlock)
                                    .fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                            .withProp(BuoyBlock.HALF, DoubleBlockHalf.LOWER)
                                    )
                            )
                            .addEntry(TransportRegistrateBlockLootTables.withExplosionDecay(buoyBlock,
                                    ItemLootEntry.builder(buoyBlock)))
                    )
            ))
            .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                    .patternLine(" P ")
                    .patternLine(" I ")
                    .patternLine("IRI")
                    .key('P', Tags.Items.GEMS_PRISMARINE)
                    .key('I', Tags.Items.INGOTS_IRON)
                    .key('R', Tags.Items.DYES_RED)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.OAK_BOAT)))
            .item(BuoyBlockItem::new)
            .model((context, provider) -> provider.generated(context, provider.modLoc("item/buoy")))
            .build()
            .register();

    public static final BlockEntry<DockBlock> DOCK = Transport.getRegistrate()
            .object("dock")
            .block(Material.IRON, DockBlock::new)
            .item()
            .build()
            .register();

    public static final BlockEntry<RailWorkerBenchBlock> RAIL_WORKER_BENCH = Transport.getRegistrate()
            .object("rail_worker_bench")
            .container(RailWorkerBenchContainer::new, () -> RailWorkerBenchScreen::new)
            .build()
            .block(RailWorkerBenchBlock::new)
            .blockstate((context, provider) -> provider.horizontalBlock(context.get(), provider.models()
                    .getExistingFile(provider.modLoc("block/rail_worker_bench")))
            )
            .lang("Rail Worker's Bench")
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                    .patternLine(" R ")
                    .patternLine("SSS")
                    .key('S', Tags.Items.STONE)
                    .key('R', ItemTags.RAILS)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(ItemTags.RAILS))
                    .build(provider)
            )
            .model((context, provider) -> provider.blockItem(context))
            .build()
            .register();

    public static final RegistryEntry<ContainerType<RailWorkerBenchContainer>> RAIL_WORKER_BENCH_CONTAINER =
            RAIL_WORKER_BENCH.getSibling(ForgeRegistries.CONTAINERS);
    //endregion

    private static NonNullUnaryOperator<AbstractBlock.Properties> railProperties() {
        return properties -> properties.hardnessAndResistance(0.7F)
                .doesNotBlockMovement()
                .sound(SoundType.METAL);
    }

    private static <B extends Block> ItemBuilder<BlockItem, BlockBuilder<B, TransportRegistrate>> createRail(
            String name, String lang, NonNullFunction<AbstractBlock.Properties, B> blockCreator) {
        return Transport.getRegistrate()
                .object(name)
                .block(Material.MISCELLANEOUS, blockCreator)
                .properties(railProperties())
                .lang(lang)
                .tag(BlockTags.RAILS)
                .item()
                .tag(ItemTags.RAILS)
                .model(TransportRegistrateModels.railItem());

    }

    public static void setup() {

    }
}

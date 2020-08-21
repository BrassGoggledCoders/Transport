package xyz.brassgoggledcoders.transport.content;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
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
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.BuoyBlock;
import xyz.brassgoggledcoders.transport.block.ModuleConfiguratorBlock;
import xyz.brassgoggledcoders.transport.block.ScaffoldingSlabBlock;
import xyz.brassgoggledcoders.transport.block.loader.EnergyLoaderBlock;
import xyz.brassgoggledcoders.transport.block.loader.FluidLoaderBlock;
import xyz.brassgoggledcoders.transport.block.loader.ItemLoaderBlock;
import xyz.brassgoggledcoders.transport.block.rail.*;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchSupportBlock;
import xyz.brassgoggledcoders.transport.block.rail.turnout.SwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.turnout.WyeSwitchRailBlock;
import xyz.brassgoggledcoders.transport.item.BuoyBlockItem;
import xyz.brassgoggledcoders.transport.registrate.RegistrateRecipes;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrate;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrateBlockLootTables;
import xyz.brassgoggledcoders.transport.tileentity.ModuleConfiguratorTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.EnergyLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.FluidLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.ItemLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.rail.TimedHoldingRailTileEntity;

import java.util.function.Function;

@SuppressWarnings("unused")
public class TransportBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Transport.ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Transport.ID);

    //region Rails
    public static final BlockEntry<HoldingRailBlock> HOLDING_RAIL =
            createRail("holding_rail", "Holding Rail", HoldingRailBlock::new)
                    .recipe(((context, recipeProvider) ->
                            ShapelessRecipeBuilder.shapelessRecipe(context.get(), 2)
                                    .addIngredient(Items.RAIL)
                                    .addIngredient(Items.POWERED_RAIL)
                                    .addCriterion("has_rail", RegistrateRecipeProvider.hasItem(ItemTags.RAILS))
                                    .build(recipeProvider)))
                    .build()
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
                    .build()
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
                    .build()
                    .register();

    public static final BlockEntry<ScaffoldingRailBlock> SCAFFOLDING_RAIL =
            createRail("scaffolding_rail", "Scaffolding Rail", ScaffoldingRailBlock::new)
                    .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get(), 3)
                            .patternLine("RRR")
                            .patternLine("SSS")
                            .key('R', Ingredient.fromItems(Items.RAIL))
                            .key('S', Ingredient.fromItems(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.getItem()))
                            .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.SCAFFOLDING))
                            .build(provider)
                    )
                    .build()
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
                    .build()
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
                    .build()
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
                    .build()
                    .register();
    public static final BlockEntry<TimedHoldingRailBlock> TIMED_HOLDING_RAIL =
            createRail("timed_holding_rail", "Timed Holding Rail", TimedHoldingRailBlock::new)
                    .recipe((context, provider) -> ShapelessRecipeBuilder.shapelessRecipe(context.get(), 2)
                            .addIngredient(Items.RAIL)
                            .addIngredient(Items.REPEATER)
                            .addCriterion("has_rail", RegistrateRecipeProvider.hasItem(ItemTags.RAILS))
                            .build(provider))
                    .build()
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
            .simpleItem()
            .recipe(RegistrateRecipes.createLoader(Tags.Items.CHESTS))
            .tileEntity(ItemLoaderTileEntity::new)
            .build()
            .register();

    public static final RegistryEntry<TileEntityType<ItemLoaderTileEntity>> ITEM_LOADER_TILE_ENTITY =
            ITEM_LOADER.getSibling(ForgeRegistries.TILE_ENTITIES);

    public static final BlockEntry<FluidLoaderBlock> FLUID_LOADER = Transport.getRegistrate()
            .block(Material.IRON, FluidLoaderBlock::new)
            .lang("Fluid Loader")
            .properties(loaderProperties())
            .simpleItem()
            .recipe(RegistrateRecipes.createLoader(Items.BUCKET))
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
            .simpleItem()
            .recipe(RegistrateRecipes.createLoader(Tags.Items.DUSTS_REDSTONE))
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
    public static final BlockRegistryObjectGroup<ScaffoldingSlabBlock, BlockItem, ?> SCAFFOLDING_SLAB_BLOCK =
            new BlockRegistryObjectGroup<>("scaffolding_slab", ScaffoldingSlabBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final RegistryObject<ElevatorSwitchSupportBlock> ELEVATOR_SWITCH_SUPPORT =
            BLOCKS.register("elevator_switch_support", ElevatorSwitchSupportBlock::new);

    public static final BlockRegistryObjectGroup<ModuleConfiguratorBlock, BlockItem, ModuleConfiguratorTileEntity> MODULE_CONFIGURATOR =
            new BlockRegistryObjectGroup<>("module_configurator", ModuleConfiguratorBlock::new, blockItemCreator(),
                    ModuleConfiguratorTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockEntry<BuoyBlock> BUOY = Transport.getRegistrate()
            .object("buoy")
            .block(Material.IRON, BuoyBlock::new)
            .lang("Buoy")
            .properties(AbstractBlock.Properties::doesNotBlockMovement)
            .properties(properties -> properties.setLightLevel(BuoyBlock::getLightLevel))
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
            .build()
            .register();
    //endregion

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        TILE_ENTITIES.register(modBus);
        ITEMS.register(modBus);
    }

    private static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(Transport.ITEM_GROUP.get()));
    }

    private static NonNullUnaryOperator<AbstractBlock.Properties> railProperties() {
        return properties -> {
            properties.hardnessAndResistance(0.7F);
            properties.doesNotBlockMovement();
            properties.sound(SoundType.METAL);
            return properties;
        };
    }

    private static <B extends Block> ItemBuilder<BlockItem, BlockBuilder<B, TransportRegistrate>> createRail(
            String name, String lang, NonNullFunction<AbstractBlock.Properties, B> blockCreator) {
        return Transport.getRegistrate()
                .object(name)
                .block(blockCreator)
                .properties(railProperties())
                .lang(lang)
                .tag(BlockTags.RAILS)
                .item()
                .tag(ItemTags.RAILS)
                .group(Transport::getItemGroup);

    }
}

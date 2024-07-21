package xyz.brassgoggledcoders.transport.content;

import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.block.BlockEntry;
import xyz.brassgoggledcoders.shadyskies.registering.block.BlockRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.blockentity.BlockEntityRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemRegisteringBuilder;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.tag.TransportItemTags;
import xyz.brassgoggledcoders.transport.block.jobsite.RailWorkerBenchBlock;
import xyz.brassgoggledcoders.transport.block.rail.*;
import xyz.brassgoggledcoders.transport.block.storage.CapabilityStorageBlock;
import xyz.brassgoggledcoders.transport.blockentity.DumpRailBlockEntity;
import xyz.brassgoggledcoders.transport.blockentity.rail.CachedRailShapeBlockEntity;
import xyz.brassgoggledcoders.transport.blockentity.rail.LoadingRailBlockEntity;
import xyz.brassgoggledcoders.transport.blockentity.storage.EnergyStorageBlockEntity;
import xyz.brassgoggledcoders.transport.blockentity.storage.FluidStorageBlockEntity;
import xyz.brassgoggledcoders.transport.data.recipe.RailWorkerBenchRecipeBuilder;

import javax.annotation.Nonnull;

;

@SuppressWarnings("unused")
public class TransportBlocks {

    public static final BlockEntry<DumpRailBlock<IItemHandler>> ITEM_DUMP_RAIL = Transport.getRegistering()
            .object("item_dump_rail")
            .block(DumpRailBlock::itemDumpRail)
            .transform(TransportBlocks::defaultRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<DumpRailBlock<IFluidHandler>> FLUID_DUMP_RAIL = Transport.getRegistering()
            .object("fluid_dump_rail")
            .block(DumpRailBlock::fluidDumpRail)
            .transform(TransportBlocks::defaultRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<DumpRailBlock<IEnergyStorage>> ENERGY_DUMP_RAIL = Transport.getRegistering()
            .object("energy_dump_rail")
            .block(DumpRailBlock::energyDumpRail)
            .transform(TransportBlocks::defaultRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntityRegisteringEntry<DumpRailBlockEntity> DUMP_RAIL_BLOCK_ENTITY = Transport.getRegistering()
            .object("dump_rail")
            .blockEntity(DumpRailBlockEntity::new)
            .withValidBlocks(
                    ITEM_DUMP_RAIL,
                    FLUID_DUMP_RAIL,
                    ENERGY_DUMP_RAIL
            )
            .register();

    public static final BlockEntry<LoadingRailBlock<IItemHandler>> ITEM_LOADING_RAIL = Transport.getRegistering()
            .object("item_loading_rail")
            .block(properties -> LoadingRailBlock.itemLoadingRail(properties, true))
            .transform(TransportBlocks::defaultRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<LoadingRailBlock<IFluidHandler>> FLUID_LOADING_RAIL = Transport.getRegistering()
            .object("fluid_loading_rail")
            .block(properties -> LoadingRailBlock.fluidDumpRail(properties, true))
            .transform(TransportBlocks::defaultRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<LoadingRailBlock<IEnergyStorage>> ENERGY_LOADING_RAIL = Transport.getRegistering()
            .object("energy_loading_rail")
            .block(properties -> LoadingRailBlock.energyDumpRail(properties, true))
            .transform(TransportBlocks::defaultRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<LoadingRailBlock<IItemHandler>> ITEM_UNLOADING_RAIL = Transport.getRegistering()
            .object("item_unloading_rail")
            .block(properties -> LoadingRailBlock.itemLoadingRail(properties, false))
            .transform(TransportBlocks::defaultRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<LoadingRailBlock<IFluidHandler>> FLUID_UNLOADING_RAIL = Transport.getRegistering()
            .object("fluid_unloading_rail")
            .block(properties -> LoadingRailBlock.fluidDumpRail(properties, false))
            .transform(TransportBlocks::defaultRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<LoadingRailBlock<IEnergyStorage>> ENERGY_UNLOADING_RAIL = Transport.getRegistering()
            .object("energy_unloading_rail")
            .block(properties -> LoadingRailBlock.energyDumpRail(properties, false))
            .transform(TransportBlocks::defaultRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntityRegisteringEntry<LoadingRailBlockEntity> LOADING_RAIL_BLOCK_ENTITY = Transport.getRegistering()
            .object("loading_rail")
            .blockEntity(LoadingRailBlockEntity::new)
            .withValidBlocks(
                    ITEM_LOADING_RAIL,
                    FLUID_LOADING_RAIL,
                    ENERGY_LOADING_RAIL,
                    ITEM_UNLOADING_RAIL,
                    FLUID_UNLOADING_RAIL,
                    ENERGY_UNLOADING_RAIL
            )
            .register();

    public static final BlockEntry<WaxedCopperRail> WAXED_OXIDIZED_COPPER_RAIL = Transport.getRegistering()
            .object("waxed_oxidized_copper_rail")
            .block(WaxedCopperRail::new)
            .transform(TransportBlocks::defaultCopperRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<WeatheringCopperRail> OXIDIZED_COPPER_RAIL = Transport.getRegistering()
            .object("oxidized_copper_rail")
            .block(properties -> new WeatheringCopperRail(properties, WeatherState.OXIDIZED))
            .transform(TransportBlocks::defaultCopperRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<WaxedCopperRail> WAXED_WEATHERED_COPPER_RAIL = Transport.getRegistering()
            .object("waxed_weathered_copper_rail")
            .block(WaxedCopperRail::new)
            .transform(TransportBlocks::defaultCopperRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<WeatheringCopperRail> WEATHERED_COPPER_RAIL = Transport.getRegistering()
            .object("weathered_copper_rail")
            .block(properties -> new WeatheringCopperRail(properties, WeatherState.WEATHERED))
            .transform(TransportBlocks::defaultCopperRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<WaxedCopperRail> WAXED_EXPOSED_COPPER_RAIL = Transport.getRegistering()
            .object("waxed_exposed_copper_rail")
            .block(WaxedCopperRail::new)
            .transform(TransportBlocks::defaultCopperRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<WeatheringCopperRail> EXPOSED_COPPER_RAIL = Transport.getRegistering()
            .object("exposed_copper_rail")
            .block(properties -> new WeatheringCopperRail(properties, WeatherState.EXPOSED))
            .transform(TransportBlocks::defaultCopperRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<WaxedCopperRail> WAXED_COPPER_RAIL = Transport.getRegistering()
            .object("waxed_copper_rail")
            .block(WaxedCopperRail::new)
            .transform(TransportBlocks::defaultCopperRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<WeatheringCopperRail> COPPER_RAIL = Transport.getRegistering()
            .object("copper_rail")
            .block(properties -> new WeatheringCopperRail(properties, WeatherState.UNAFFECTED))
            .transform(TransportBlocks::defaultCopperRail)
            .transform(TransportBlocks::defaultRailItem)
            .build()
            .register();

    public static final BlockEntry<OneWayBoosterRailBlock> ONE_WAY_BOOSTER_RAIL = Transport.getRegistering()
            .object("one_way_booster_rail")
            .block(OneWayBoosterRailBlock::new)
            .transform(TransportBlocks::defaultRail)
            .blockstate(BlockModelHelper::straightPoweredInvertedRailBlockState)
            .transform(TransportBlocks::defaultRailItem)
            .tag(TransportItemTags.RAILS_GOLD)
            .recipe((context, provider) -> RailWorkerBenchRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(TransportItemTags.RAILS_GOLD))
                    .save(provider)
            )
            .build()
            .register();

    public static final BlockEntry<SwitchRailBlock> SWITCH_RAIL = Transport.getRegistering()
            .object("switch_rail")
            .block(SwitchRailBlock::new)
            .transform(TransportBlocks::defaultRail)
            .blockstate(BlockModelHelper::switchRail)
            .item()
            .model((context, provider) -> provider.generated(context, provider.modLoc("block/rail/switch_rail_straight_right")))
            .tag(ItemTags.RAILS)
            .tag(TransportItemTags.RAILS_IRON)
            .recipe((context, provider) -> RailWorkerBenchRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                    .save(provider)
            )
            .build()
            .register();

    public static final BlockEntry<WyeSwitchRailBlock> WYE_SWITCH_RAIL = Transport.getRegistering()
            .object("wye_switch_rail")
            .block(WyeSwitchRailBlock::new)
            .transform(TransportBlocks::defaultRail)
            .blockstate(BlockModelHelper::wyeSwitchRail)
            .transform(TransportBlocks::defaultRailItem)
            .tag(TransportItemTags.RAILS_IRON)
            .recipe((context, provider) -> RailWorkerBenchRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                    .save(provider)
            )
            .build()
            .register();

    public static final BlockEntry<DiamondCrossingRailBlock> DIAMOND_CROSSING_RAIL = Transport.getRegistering()
            .object("diamond_crossing_rail")
            .block(DiamondCrossingRailBlock::new)
            .transform(TransportBlocks::defaultRail)
            .blockstate((context, provider) -> provider.simpleBlock(
                    context.get(),
                    provider.models()
                            .getBuilder("block/" + context.getName())
                            .parent(provider.models()
                                    .getExistingFile(provider.mcLoc("block/rail_flat"))
                            )
                            .texture("rail", provider.modLoc("block/rail/" + context.getName()))
            ))
            .transform(TransportBlocks::defaultRailItem)
            .tag(TransportItemTags.RAILS_IRON)
            .build()
            .register();


    public static final BlockEntityRegisteringEntry<CachedRailShapeBlockEntity> CACHED_RAIL_SHAPE_BLOCK_ENTITY = Transport.getRegistering()
            .object("cached_rail_shape")
            .blockEntity(CachedRailShapeBlockEntity::new)
            .withValidBlocks(SWITCH_RAIL, WYE_SWITCH_RAIL)
            .register();

    public static final BlockEntry<BufferRailBlock> BUMPER_RAIL = Transport.getRegistering()
            .object("buffer_rail")
            .block(BufferRailBlock::new)
            .withProperties(properties -> properties.noOcclusion()
                    .strength(2.1F)
                    .sound(SoundType.METAL)
            )
            .tag(BlockTags.RAILS)
            .blockstate((context, provider) -> BlockModelHelper.straightInvertedFlatRailBlockState(
                    context,
                    provider,
                    provider.modLoc("block/buffer_rail")
            ))
            .item()
            .tag(ItemTags.RAILS)
            .tag(TransportItemTags.RAILS_IRON)
            .recipe((context, provider) -> RailWorkerBenchRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(TransportItemTags.RAILS_IRON))
                    .save(provider)
            )
            .build()
            .register();

    public static final BlockEntry<InvertedPoweredRailBlock> INVERTED_POWERED_RAIL = Transport.getRegistering()
            .object("inverted_powered_rail")
            .block(InvertedPoweredRailBlock::new)
            .transform(TransportBlocks::defaultRail)
            .blockstate((context, provider) -> BlockModelHelper.straightPoweredRailBlockState(
                    context,
                    provider,
                    provider.mcLoc("block/powered_rail"),
                    provider.mcLoc("block/powered_rail_on")
            ))
            .item()
            .model((context, provider) -> provider.generated(context, provider.mcLoc("block/powered_rail_on")))
            .tag(ItemTags.RAILS)
            .tag(TransportItemTags.RAILS_GOLD)
            .recipe((context, provider) -> RailWorkerBenchRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(TransportItemTags.RAILS_GOLD))
                    .save(provider)
            )
            .build()
            .register();

    public static final BlockEntry<CapabilityStorageBlock<FluidStorageBlockEntity>> FLUID_STORAGE = Transport.getRegistering()
            .object("fluid_storage")
            .block(properties -> new CapabilityStorageBlock<>(properties, FluidStorageBlockEntity::new))
            .blockstate(BlockModelHelper::storageBlock)
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get())
                    .pattern("III")
                    .pattern("GBG")
                    .pattern("III")
                    .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                    .define('G', Ingredient.of(Tags.Items.GLASS))
                    .define('B', Ingredient.of(Items.BUCKET))
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(Items.BUCKET))
                    .save(provider)
            )
            .build()
            .blockEntity(FluidStorageBlockEntity::new)
            .build()
            .register();

    public static final BlockEntry<CapabilityStorageBlock<EnergyStorageBlockEntity>> ENERGY_STORAGE = Transport.getRegistering()
            .object("energy_storage")
            .block(properties -> new CapabilityStorageBlock<>(properties, EnergyStorageBlockEntity::new))
            .blockstate(BlockModelHelper::storageBlock)
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get())
                    .pattern("III")
                    .pattern("GRG")
                    .pattern("III")
                    .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                    .define('G', Ingredient.of(Tags.Items.GLASS))
                    .define('R', Ingredient.of(Tags.Items.STORAGE_BLOCKS_REDSTONE))
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(Tags.Items.STORAGE_BLOCKS_REDSTONE))
                    .save(provider)
            )
            .build()
            .blockEntity(EnergyStorageBlockEntity::new)
            .build()
            .register();

    public static final BlockEntry<RailWorkerBenchBlock> RAIL_WORKER_BENCH = Transport.getRegistering()
            .object("rail_worker_bench")
            .block(RailWorkerBenchBlock::new)
            .blockstate((context, provider) -> provider.simpleBlock(
                    context.get(),
                    provider.models().getExistingFile(provider.modLoc("block/rail_worker_bench"))
            ))
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get())
                    .pattern("RRR")
                    .pattern("SSS")
                    .define('R', ItemTags.RAILS)
                    .define('S', Tags.Items.STONE)
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(ItemTags.RAILS))
                    .save(provider)
            )
            .build()
            .register();

    @Nonnull
    public static <T extends BaseRailBlock> BlockRegisteringBuilder<Registering, T> defaultRail(BlockRegisteringBuilder<Registering, T> builder) {
        return builder.withProperties(properties -> properties.noCollission()
                .strength(0.7F)
                .sound(SoundType.METAL)
        );
    }

    @Nonnull
    public static <T extends BaseRailBlock> BlockRegisteringBuilder<Registering, T> defaultCopperRail(BlockRegisteringBuilder<Registering, T> builder) {
        return builder.withProperties(properties -> properties.noCollission()
                .strength(0.7F)
                .sound(SoundType.COPPER)
                .mapColor(MapColor.COLOR_ORANGE)
        );
    }


    @Nonnull
    public static <T extends BaseRailBlock> ItemRegisteringBuilder<BlockRegisteringBuilder<Registering, T>, BlockItem> defaultRailItem(
            BlockRegisteringBuilder<Registering, T> builder
    ) {
        return builder.withItem();
    }

    public static void setup() {

    }
}

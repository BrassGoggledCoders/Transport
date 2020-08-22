package xyz.brassgoggledcoders.transport.content;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.ModuleConfiguratorBlock;
import xyz.brassgoggledcoders.transport.block.ScaffoldingSlabBlock;
import xyz.brassgoggledcoders.transport.block.loader.LoaderBlock;
import xyz.brassgoggledcoders.transport.block.rail.*;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchSupportBlock;
import xyz.brassgoggledcoders.transport.block.rail.turnout.SwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.turnout.WyeSwitchRailBlock;
import xyz.brassgoggledcoders.transport.tileentity.ModuleConfiguratorTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.EnergyLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.FluidLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.ItemLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.rail.SwitchRailTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.rail.TimedHoldingRailTileEntity;

import java.util.function.Function;

@SuppressWarnings("unused")
public class TransportBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Transport.ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Transport.ID);

    //region Rails
    public static final BlockRegistryObjectGroup<HoldingRailBlock, BlockItem, ?> HOLDING_RAIL =
            new BlockRegistryObjectGroup<>("holding_rail", HoldingRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<DiamondCrossingRailBlock, BlockItem, ?> DIAMOND_CROSSING_RAIL =
            new BlockRegistryObjectGroup<>("diamond_crossing_rail", DiamondCrossingRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<ElevatorSwitchRailBlock, BlockItem, ?> ELEVATOR_SWITCH_RAIL =
            new BlockRegistryObjectGroup<>("elevator_switch_rail", ElevatorSwitchRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<ScaffoldingRailBlock, BlockItem, ?> SCAFFOLDING_RAIL =
            new BlockRegistryObjectGroup<>("scaffolding_rail", ScaffoldingRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<SwitchRailBlock, BlockItem, ?> SWITCH_RAIL =
            new BlockRegistryObjectGroup<>("switch_rail", SwitchRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<WyeSwitchRailBlock, BlockItem, ?> WYE_SWITCH_RAIL =
            new BlockRegistryObjectGroup<>("wye_switch_rail", WyeSwitchRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<BumperRailBlock, BlockItem, ?> BUMPER_RAIL =
            new BlockRegistryObjectGroup<>("bumper_rail", BumperRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<TimedHoldingRailBlock, BlockItem, TimedHoldingRailTileEntity> TIMED_HOLDING_RAIL =
            new BlockRegistryObjectGroup<>("timed_holding_rail", () ->
                    new TimedHoldingRailBlock(railProperties()),
                    blockItemCreator(), TimedHoldingRailTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final RegistryObject<TileEntityType<SwitchRailTileEntity>> SWITCH_RAIL_TILE_ENTITY = TILE_ENTITIES
            .register("switch_rail", () -> TileEntityType.Builder.create(SwitchRailTileEntity::new,
                    SWITCH_RAIL.get(), WYE_SWITCH_RAIL.get())
                    .build(null));
    //endregion

    public static AbstractBlock.Properties railProperties() {
        return AbstractBlock.Properties.create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0.7F)
                .sound(SoundType.METAL);
    }

    //region Loaders
    public static final BlockRegistryObjectGroup<LoaderBlock, BlockItem, ItemLoaderTileEntity> ITEM_LOADER =
            new BlockRegistryObjectGroup<>("item_loader", () -> new LoaderBlock(ItemLoaderTileEntity::new),
                    blockItemCreator(), ItemLoaderTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<LoaderBlock, BlockItem, FluidLoaderTileEntity> FLUID_LOADER =
            new BlockRegistryObjectGroup<>("fluid_loader", () -> new LoaderBlock(FluidLoaderTileEntity::new),
                    blockItemCreator(), FluidLoaderTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<LoaderBlock, BlockItem, EnergyLoaderTileEntity> ENERGY_LOADER =
            new BlockRegistryObjectGroup<>("energy_loader", () -> new LoaderBlock(EnergyLoaderTileEntity::new),
                    blockItemCreator(), EnergyLoaderTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);
    //endregion

    //region Assorted
    public static final BlockRegistryObjectGroup<ScaffoldingSlabBlock, BlockItem, ?> SCAFFOLDING_SLAB_BLOCK =
            new BlockRegistryObjectGroup<>("scaffolding_slab", ScaffoldingSlabBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final RegistryObject<ElevatorSwitchSupportBlock> ELEVATOR_SWITCH_SUPPORT =
            BLOCKS.register("elevator_switch_support", ElevatorSwitchSupportBlock::new);

    public static final BlockRegistryObjectGroup<ModuleConfiguratorBlock, BlockItem, ModuleConfiguratorTileEntity> MODULE_CONFIGURATOR =
            new BlockRegistryObjectGroup<>(
                    "module_configurator",
                    ModuleConfiguratorBlock::new,
                    blockItemCreator(),
                    ModuleConfiguratorTileEntity::new
            ).register(
                    BLOCKS,
                    ITEMS,
                    TILE_ENTITIES
            );
    //endregion

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        TILE_ENTITIES.register(modBus);
        ITEMS.register(modBus);
    }

    private static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(Transport.ITEM_GROUP));
    }
}

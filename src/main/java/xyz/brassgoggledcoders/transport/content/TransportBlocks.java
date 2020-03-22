package xyz.brassgoggledcoders.transport.content;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.ScaffoldingSlabBlock;
import xyz.brassgoggledcoders.transport.block.loader.LoaderBlock;
import xyz.brassgoggledcoders.transport.block.rail.DiamondCrossingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.HoldingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.ScaffoldingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchSupportBlock;
import xyz.brassgoggledcoders.transport.block.rail.turnout.SwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.switchmotor.RedstoneSwitchMotorBlock;
import xyz.brassgoggledcoders.transport.tileentity.loader.EnergyLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.FluidLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.ItemLoaderTileEntity;

import java.util.function.Function;

@SuppressWarnings("unused")
public class TransportBlocks {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Transport.ID);
    public static final RegistryObject<ElevatorSwitchSupportBlock> ELEVATOR_SWITCH_SUPPORT =
            BLOCKS.register("elevator_switch_support", ElevatorSwitchSupportBlock::new);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Transport.ID);
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
    //endregion
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

    //region Switch Motors
    public static final BlockRegistryObjectGroup<RedstoneSwitchMotorBlock, BlockItem, ?> REDSTONE_SWITCH_MOTOR =
            new BlockRegistryObjectGroup<>("redstone_switch_motor", RedstoneSwitchMotorBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    //endregion

    //region Assorted
    public static final BlockRegistryObjectGroup<ScaffoldingSlabBlock, BlockItem, ?> SCAFFOLDING_SLAB_BLOCK =
            new BlockRegistryObjectGroup<>("scaffolding_slab", ScaffoldingSlabBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
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

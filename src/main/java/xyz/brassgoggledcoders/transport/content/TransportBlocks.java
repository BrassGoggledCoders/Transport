package xyz.brassgoggledcoders.transport.content;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.loader.LoaderBlock;
import xyz.brassgoggledcoders.transport.block.rail.DiamondCrossingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.HoldingRailBlock;
import xyz.brassgoggledcoders.transport.container.loader.LoaderContainer;
import xyz.brassgoggledcoders.transport.tileentity.loader.EnergyLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.FluidLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.ItemLoaderTileEntity;

import java.util.function.Function;

@SuppressWarnings("unused")
public class TransportBlocks {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Transport.ID);
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

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        TILE_ENTITIES.register(modBus);
        ITEMS.register(modBus);
    }

    private static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(Transport.ITEM_GROUP));
    }
}

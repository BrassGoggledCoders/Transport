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
import xyz.brassgoggledcoders.transport.block.loader.ItemLoaderBlock;
import xyz.brassgoggledcoders.transport.block.rail.DiamondCrossingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.HoldingRailBlock;
import xyz.brassgoggledcoders.transport.tileentity.loader.ItemLoaderTileEntity;

@SuppressWarnings({"ConstantConditions", "unused"})
public class TransportBlocks {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Transport.ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Transport.ID);

    //region Rails
    public static final RegistryObject<HoldingRailBlock> HOLDING_RAIL = BLOCKS.register("holding_rail",
            HoldingRailBlock::new);
    public static final RegistryObject<BlockItem> HOLDING_RAIL_ITEM = ITEMS.register("holding_rail",
            () -> new BlockItem(HOLDING_RAIL.get(), new Item.Properties()
                    .group(Transport.ITEM_GROUP)));

    public static final RegistryObject<DiamondCrossingRailBlock> DIAMOND_CROSSING_RAIL = BLOCKS.register("diamond_crossing_rail",
            DiamondCrossingRailBlock::new);
    public static final RegistryObject<BlockItem> DIAMOND_CROSSING_RAIL_ITEM = ITEMS.register("diamond_crossing_rail",
            () -> new BlockItem(DIAMOND_CROSSING_RAIL.get(), new Item.Properties()
                    .group(Transport.ITEM_GROUP)));
    //endregion

    //region Loaders
    public static final RegistryObject<ItemLoaderBlock> ITEM_LOADER = BLOCKS.register("item_loader", ItemLoaderBlock::new);
    public static final RegistryObject<TileEntityType<ItemLoaderTileEntity>> ITEM_LOADER_TILE_ENTITY =
            TILE_ENTITIES.register("item_loader", () -> TileEntityType.Builder.create(ItemLoaderTileEntity::new,
                    ITEM_LOADER.get()).build(null));
    public static final RegistryObject<BlockItem> ITEM_LOADER_ITEM = ITEMS.register("item_loader", () ->
            new BlockItem(ITEM_LOADER.get(), new Item.Properties()
                    .group(Transport.ITEM_GROUP)));
    //endregion

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        TILE_ENTITIES.register(modBus);
        ITEMS.register(modBus);
    }
}

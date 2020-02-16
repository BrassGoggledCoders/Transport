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
import xyz.brassgoggledcoders.transport.block.rail.DiamondCrossingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.HoldingRailBlock;

@SuppressWarnings("unused")
public class TransportBlocks {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Transport.ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Transport.ID);

    public static final RegistryObject<HoldingRailBlock> HOLDING_RAIL = BLOCKS.register("holding_rail",
            HoldingRailBlock::new);
    public static final RegistryObject<BlockItem> HOLDING_RAIL_ITEM = ITEMS.register("holding_rail",
            () -> new BlockItem(HOLDING_RAIL.get(), new Item.Properties()
                    .group(Transport.instance.transportGroup)));

    public static final RegistryObject<DiamondCrossingRailBlock> DIAMOND_CROSSING_RAIL = BLOCKS.register("diamond_crossing_rail",
            DiamondCrossingRailBlock::new);
    public static final RegistryObject<BlockItem> DIAMOND_CROSSING_RAIL_ITEM = ITEMS.register("diamond_crossing_rail",
            () -> new BlockItem(DIAMOND_CROSSING_RAIL.get(), new Item.Properties()
                    .group(Transport.instance.transportGroup)));

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        TILE_ENTITIES.register(modBus);
        ITEMS.register(modBus);
    }
}

package xyz.brassgoggledcoders.transport.content;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class TransportWeathering {
    public static final Supplier<BiMap<Block, Block>> WAX_ON = Suppliers.memoize(
            () -> ImmutableBiMap.<Block, Block>builder()
                    .put(TransportBlocks.COPPER_RAIL.get(), TransportBlocks.WAXED_COPPER_RAIL.get())
                    .put(TransportBlocks.EXPOSED_COPPER_RAIL.get(), TransportBlocks.WAXED_EXPOSED_COPPER_RAIL.get())
                    .put(TransportBlocks.WEATHERED_COPPER_RAIL.get(), TransportBlocks.WAXED_WEATHERED_COPPER_RAIL.get())
                    .put(TransportBlocks.OXIDIZED_COPPER_RAIL.get(), TransportBlocks.WAXED_OXIDIZED_COPPER_RAIL.get())
                    .build()
    );

    public static final Supplier<BiMap<Block, Block>> WAX_OFF = Suppliers.memoize(
            () -> WAX_ON.get()
                    .inverse()
    );

    public static final Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(
            () -> ImmutableBiMap.<Block, Block>builder()
                    .put(TransportBlocks.COPPER_RAIL.get(), TransportBlocks.EXPOSED_COPPER_RAIL.get())
                    .put(TransportBlocks.EXPOSED_COPPER_RAIL.get(), TransportBlocks.WEATHERED_COPPER_RAIL.get())
                    .put(TransportBlocks.WEATHERED_COPPER_RAIL.get(), TransportBlocks.OXIDIZED_COPPER_RAIL.get())
                    .build()
    );
    public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(
            () -> NEXT_BY_BLOCK.get()
                    .inverse()
    );
}

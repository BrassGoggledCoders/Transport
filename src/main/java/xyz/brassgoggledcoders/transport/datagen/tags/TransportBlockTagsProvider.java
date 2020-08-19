package xyz.brassgoggledcoders.transport.datagen.tags;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.content.TransportItems;

import javax.annotation.Nonnull;

public class TransportBlockTagsProvider extends BlockTagsProvider {
    public TransportBlockTagsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(BlockTags.RAILS)
                .add(
                        TransportBlocks.BUMPER_RAIL.getBlock(),
                        TransportBlocks.DIAMOND_CROSSING_RAIL.getBlock(),
                        TransportBlocks.ELEVATOR_SWITCH_RAIL.getBlock(),
                        TransportBlocks.HOLDING_RAIL.getBlock(),
                        TransportBlocks.SCAFFOLDING_RAIL.getBlock(),
                        TransportBlocks.SWITCH_RAIL.getBlock(),
                        TransportBlocks.WYE_SWITCH_RAIL.getBlock(),
                        TransportBlocks.TIMED_HOLDING_RAIL.getBlock()
                );
    }

    @Override
    @Nonnull
    public String getName() {
        return "Transport Block Tags";
    }
}

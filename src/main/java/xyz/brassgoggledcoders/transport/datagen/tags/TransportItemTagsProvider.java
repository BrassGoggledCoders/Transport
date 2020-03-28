package xyz.brassgoggledcoders.transport.datagen.tags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.content.TransportItems;

import javax.annotation.Nonnull;

public class TransportItemTagsProvider extends ItemTagsProvider {
    public TransportItemTagsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(TransportItemTags.WRENCHES).add(TransportItems.RAIL_BREAKER.get());

        this.getBuilder(ItemTags.RAILS)
                .add(
                        TransportBlocks.BUMPER_RAIL.getItem(),
                        TransportBlocks.DIAMOND_CROSSING_RAIL.getItem(),
                        TransportBlocks.ELEVATOR_SWITCH_RAIL.getItem(),
                        TransportBlocks.HOLDING_RAIL.getItem(),
                        TransportBlocks.SCAFFOLDING_RAIL.getItem(),
                        TransportBlocks.SWITCH_RAIL.getItem(),
                        TransportBlocks.WYE_SWITCH_RAIL.getItem()
                );
    }

    @Override
    @Nonnull
    public String getName() {
        return "Transport Item Tags";
    }
}

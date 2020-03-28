package xyz.brassgoggledcoders.transport.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import xyz.brassgoggledcoders.transport.Transport;

public class RailBreakerItem extends Item {
    public RailBreakerItem() {
        this(new Properties()
                .maxStackSize(1)
                .group(Transport.ITEM_GROUP)
        );
    }

    public RailBreakerItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.isIn(BlockTags.RAILS) ? 15F : 1F;
    }
}

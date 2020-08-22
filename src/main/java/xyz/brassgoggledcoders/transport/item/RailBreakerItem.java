package xyz.brassgoggledcoders.transport.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;

import javax.annotation.Nonnull;

public class RailBreakerItem extends Item {
    public RailBreakerItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
        return state.isIn(BlockTags.RAILS) ? 15F : 1F;
    }
}

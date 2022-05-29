package xyz.brassgoggledcoders.transport.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class RailBreakerItem extends Item {
    public RailBreakerItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
        return state.is(BlockTags.RAILS) ? 15F : super.getDestroySpeed(stack, state);
    }
}

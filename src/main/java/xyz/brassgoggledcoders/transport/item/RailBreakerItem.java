package xyz.brassgoggledcoders.transport.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.BlockFlags;

import javax.annotation.Nonnull;

public class RailBreakerItem extends Item {
    public RailBreakerItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
        return state.isIn(BlockTags.RAILS) ? 15F : 1F;
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        BlockPos blockPos = context.getPos();
        World world = context.getWorld();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState oldBlockState = world.getBlockState(blockPos);
        if (oldBlockState.isIn(BlockTags.RAILS)) {
            Rotation rotation = playerEntity == null || !playerEntity.isCrouching() ?
                    Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90;
            BlockState newBlockState = oldBlockState.rotate(world, blockPos, rotation);
            if (newBlockState != oldBlockState && newBlockState != null) {
                if (world.setBlockState(blockPos, newBlockState, BlockFlags.DEFAULT_AND_RERENDER)) {
                    return ActionResultType.func_233537_a_(world.isRemote());
                } else {
                    return ActionResultType.FAIL;
                }
            }
        }
        return super.onItemUse(context);
    }
}

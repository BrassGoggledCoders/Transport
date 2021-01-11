package xyz.brassgoggledcoders.transport.item;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.MinecartItem;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BasicMinecartItem extends MinecartItem {
    public BasicMinecartItem(Properties builder) {
        super(AbstractMinecartEntity.Type.CHEST, builder);
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (!blockstate.isIn(BlockTags.RAILS)) {
            return ActionResultType.FAIL;
        } else {
            ItemStack itemstack = context.getItem();
            if (!world.isRemote) {
                RailShape railshape = blockstate.getBlock() instanceof AbstractRailBlock ?
                        ((AbstractRailBlock) blockstate.getBlock()).getRailDirection(blockstate, world, blockpos, null) :
                        RailShape.NORTH_SOUTH;
                double heightOffset = 0.0D;
                if (railshape.isAscending()) {
                    heightOffset = 0.5D;
                }

                AbstractMinecartEntity minecartEntity = create(context, heightOffset);
                if (minecartEntity != null) {
                    if (itemstack.hasDisplayName()) {
                        minecartEntity.setCustomName(itemstack.getDisplayName());
                    }

                    world.addEntity(minecartEntity);
                }
            }

            itemstack.shrink(1);
            return ActionResultType.func_233537_a_(world.isRemote);
        }
    }

    @Nullable
    protected abstract AbstractMinecartEntity create(ItemUseContext itemUseContext, double heightOffset);
}

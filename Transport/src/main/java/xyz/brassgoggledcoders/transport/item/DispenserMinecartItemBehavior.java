package xyz.brassgoggledcoders.transport.item;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class DispenserMinecartItemBehavior extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
    private final Function3<ItemStack, Level, Vec3, ? extends AbstractMinecart> minecartCreator;

    public DispenserMinecartItemBehavior(Function3<ItemStack, Level, Vec3, ? extends AbstractMinecart> minecartCreator) {
        this.minecartCreator = minecartCreator;
    }

    @Override
    @Nonnull
    public ItemStack execute(BlockSource blockSource, @Nonnull ItemStack pStack) {
        Direction direction = blockSource.getBlockState().getValue(DispenserBlock.FACING);
        Level level = blockSource.getLevel();
        double d0 = blockSource.x() + (double) direction.getStepX() * 1.125D;
        double d1 = Math.floor(blockSource.y()) + (double) direction.getStepY();
        double d2 = blockSource.z() + (double) direction.getStepZ() * 1.125D;
        BlockPos blockpos = blockSource.getPos().relative(direction);
        BlockState blockstate = level.getBlockState(blockpos);
        RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock ?
                ((BaseRailBlock) blockstate.getBlock()).getRailDirection(blockstate, level, blockpos, null) :
                RailShape.NORTH_SOUTH;
        double slopeOffset;
        if (blockstate.is(BlockTags.RAILS)) {
            if (railshape.isAscending()) {
                slopeOffset = 0.6D;
            } else {
                slopeOffset = 0.1D;
            }
        } else {
            if (!blockstate.isAir() || !level.getBlockState(blockpos.below()).is(BlockTags.RAILS)) {
                return this.defaultDispenseItemBehavior.dispense(blockSource, pStack);
            }

            BlockState blockStateBelow = level.getBlockState(blockpos.below());
            RailShape railShapeBelow = blockStateBelow.getBlock() instanceof BaseRailBlock ?
                    ((BaseRailBlock) blockstate.getBlock()).getRailDirection(blockstate, level, blockpos, null) :
                    RailShape.NORTH_SOUTH;
            if (direction != Direction.DOWN && railShapeBelow.isAscending()) {
                slopeOffset = -0.4D;
            } else {
                slopeOffset = -0.9D;
            }
        }

        AbstractMinecart minecart = minecartCreator.apply(
                pStack,
                level,
                new Vec3(d0, d1 + slopeOffset, d2)
        );

        if (pStack.hasCustomHoverName()) {
            minecart.setCustomName(pStack.getHoverName());
        }

        level.addFreshEntity(minecart);
        pStack.shrink(1);
        return pStack;
    }
}

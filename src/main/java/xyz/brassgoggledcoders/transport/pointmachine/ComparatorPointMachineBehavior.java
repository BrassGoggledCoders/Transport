package xyz.brassgoggledcoders.transport.pointmachine;

import net.minecraft.block.*;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.CommandBlockMinecartEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ComparatorMode;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import xyz.brassgoggledcoders.transport.api.pointmachine.IPointMachineBehavior;

import javax.annotation.Nullable;

public class ComparatorPointMachineBehavior implements IPointMachineBehavior {
    @Override
    public boolean shouldDiverge(BlockState motorState, IBlockReader blockReader, BlockPos motorPos, BlockPos switchPos, @Nullable AbstractMinecartEntity minecartEntity) {
        if (minecartEntity != null) {
            Direction direction = motorState.get(BlockStateProperties.HORIZONTAL_FACING);
            if (motorPos.offset(direction).equals(switchPos)) {
                int power = 0;
                if (blockReader instanceof IWorldReader) {
                    power = this.getPowerOnSides((IWorldReader) blockReader, motorPos, direction);
                }
                int comparatorLevel = minecartEntity.getComparatorLevel();
                if (comparatorLevel < 0) {
                    if (minecartEntity instanceof CommandBlockMinecartEntity) {
                        comparatorLevel = ((CommandBlockMinecartEntity) minecartEntity).getCommandBlockLogic().getSuccessCount();
                    } else if (minecartEntity instanceof IInventory) {
                        comparatorLevel = Container.calcRedstoneFromInventory((IInventory)minecartEntity);
                    }
                }
                return (motorState.get(ComparatorBlock.MODE) == ComparatorMode.COMPARE) == (comparatorLevel >= power);
            }
        }
        return false;
    }

    protected int getPowerOnSides(IWorldReader world, BlockPos pos, Direction direction) {
        Direction direction1 = direction.rotateY();
        Direction direction2 = direction.rotateYCCW();
        return Math.max(this.getPowerOnSide(world, pos.offset(direction1), direction1), this.getPowerOnSide(world, pos.offset(direction2), direction2));
    }

    protected int getPowerOnSide(IWorldReader world, BlockPos pos, Direction side) {
        BlockState blockstate = world.getBlockState(pos);
        Block block = blockstate.getBlock();
        if (blockstate.canProvidePower()) {
            if (block == Blocks.REDSTONE_BLOCK) {
                return 15;
            } else {
                return block == Blocks.REDSTONE_WIRE ? blockstate.get(RedstoneWireBlock.POWER) :
                        world.getStrongPower(pos, side);
            }
        } else {
            return 0;
        }
    }
}

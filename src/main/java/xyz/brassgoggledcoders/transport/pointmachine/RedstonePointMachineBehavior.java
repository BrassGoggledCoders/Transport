package xyz.brassgoggledcoders.transport.pointmachine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.transport.api.pointmachine.IPointMachineBehavior;

import javax.annotation.Nullable;

public class RedstonePointMachineBehavior implements IPointMachineBehavior {
    @Override
    public boolean shouldDiverge(BlockState motorState, IBlockReader blockReader, BlockPos motorPos, BlockPos switchPos,
                                 @Nullable AbstractMinecartEntity minecartEntity) {
        return motorState.get(BlockStateProperties.POWERED) && (!motorState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                || (motorPos.offset(motorState.get(BlockStateProperties.HORIZONTAL_FACING).getOpposite()).equals(switchPos)));
    }
}

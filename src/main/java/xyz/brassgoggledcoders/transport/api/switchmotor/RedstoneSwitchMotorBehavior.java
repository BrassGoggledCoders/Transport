package xyz.brassgoggledcoders.transport.api.switchmotor;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class RedstoneSwitchMotorBehavior implements ISwitchMotorBehavior {
    @Override
    public boolean shouldDiverge(BlockState motorState, IBlockReader blockReader, BlockPos motorPos, BlockPos switchPos,
                                 @Nullable AbstractMinecartEntity minecartEntity) {
        return motorState.get(BlockStateProperties.POWERED) && (!motorState.has(BlockStateProperties.HORIZONTAL_FACING)
                || (motorPos.offset(motorState.get(BlockStateProperties.HORIZONTAL_FACING)).equals(switchPos)));
    }
}

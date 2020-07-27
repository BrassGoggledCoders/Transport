package xyz.brassgoggledcoders.transport.pointmachine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.transport.api.pointmachine.IPointMachineBehavior;

public class LeverPointMachineBehavior implements IPointMachineBehavior {
    @Override
    public boolean shouldDiverge(BlockState motorState, IBlockReader blockReader, BlockPos motorPos, BlockPos switchPos,
                                 AbstractMinecartEntity minecartEntity) {
        if (motorState.hasProperty(BlockStateProperties.FACE) && motorState.hasProperty(BlockStateProperties.POWERED)) {
            return motorState.get(BlockStateProperties.FACE) == AttachFace.FLOOR &&
                    motorState.get(BlockStateProperties.POWERED) &&
                    (!motorState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) || motorPos.offset(motorState.get(
                            BlockStateProperties.HORIZONTAL_FACING)).equals(switchPos));
        }

        return false;
    }
}

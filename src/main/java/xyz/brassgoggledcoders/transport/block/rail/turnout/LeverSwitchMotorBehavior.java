package xyz.brassgoggledcoders.transport.block.rail.turnout;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeverBlock;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.transport.api.switchmotor.ISwitchMotorBehavior;

public class LeverSwitchMotorBehavior implements ISwitchMotorBehavior {
    @Override
    public boolean shouldDiverge(BlockState motorState, IBlockReader blockReader, BlockPos motorPos, BlockPos switchPos,
                                 AbstractMinecartEntity minecartEntity) {
        if (motorState.getBlock() == Blocks.LEVER) {
            return motorState.get(LeverBlock.FACE) == AttachFace.FLOOR && motorState.get(LeverBlock.POWERED) &&
                    motorPos.offset(motorState.get(LeverBlock.HORIZONTAL_FACING)).equals(switchPos);
        }

        return false;
    }
}

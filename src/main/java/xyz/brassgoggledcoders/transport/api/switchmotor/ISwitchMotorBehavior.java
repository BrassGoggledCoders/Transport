package xyz.brassgoggledcoders.transport.api.switchmotor;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public interface ISwitchMotorBehavior {
    boolean shouldDiverge(BlockState motorState, IBlockReader blockReader, BlockPos motorPos, BlockPos switchPos,
                          @Nullable AbstractMinecartEntity minecartEntity);
}

package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CopperRail extends RailBlock {
    public CopperRail(Properties properties) {
        super(properties);
    }

    @Override
    public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        return super.getRailMaxSpeed(state, level, pos, cart) / 2;
    }

    @Override
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (state.getValue(this.getShapeProperty()).isAscending() &&
                level.getBlockState(pos.above()).isFaceSturdy(level, pos, Direction.DOWN, SupportType.RIGID)) {
            cart.setDeltaMovement(Vec3.ZERO);
        } else {
            cart.setDeltaMovement(cart.getDeltaMovement().scale(1.2));
        }

    }
}

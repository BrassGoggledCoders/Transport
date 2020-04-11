package xyz.brassgoggledcoders.transport.block.rail.turnout;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.pointmachine.IPointMachineBehavior;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class AbstractSwitchRailBlock extends AbstractRailBlock {
    public static final BooleanProperty DIVERGE = BooleanProperty.create("diverge");

    protected AbstractSwitchRailBlock(boolean disableCorner, Properties properties) {
        super(disableCorner, properties);
    }

    protected boolean shouldDivert(IBlockReader blockReader, BlockPos motorLocation, BlockPos switchLocation,
                                   @Nullable AbstractMinecartEntity cart) {
        BlockState motorState = blockReader.getBlockState(motorLocation);
        IPointMachineBehavior motorBehavior = TransportAPI.getPointMachineBehavior(motorState.getBlock());
        if (motorBehavior != null) {
            return motorBehavior.shouldDiverge(motorState, blockReader, motorLocation, switchLocation, cart);
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world,
                                          BlockPos currentPos, BlockPos facingPos) {
        SwitchConfiguration configuration = this.getSwitchConfiguration(state);
        if (this.getMotorDirection(configuration) == facing) {
            BlockPos motorPos = currentPos.offset(facing);
            BlockState motorState = world.getBlockState(motorPos);
            IPointMachineBehavior motorBehavior = TransportAPI.getPointMachineBehavior(motorState.getBlock());
            if (motorBehavior != null) {
                motorBehavior.onBlockStateUpdate(motorState, world, motorPos);
                state = state.with(DIVERGE, motorBehavior.shouldDiverge(motorState, world, motorPos, currentPos, null));
            }
        }
        return state;
    }

    @Override
    @Nonnull
    public RailShape getRailDirection(BlockState state, IBlockReader blockReader, BlockPos pos,
                                      @Nullable AbstractMinecartEntity minecartEntity) {
        SwitchConfiguration switchConfiguration = getSwitchConfiguration(state);
        RailShape straightShape = getStraightShape(switchConfiguration);
        RailShape divergeShape = getDivergeShape(switchConfiguration);

        if (minecartEntity != null) {
            Direction entrance = minecartEntity.getAdjustedHorizontalFacing().getOpposite();
            if (entrance == switchConfiguration.getNarrowSide()) {
                if (shouldDivert(blockReader, pos.offset(this.getMotorDirection(switchConfiguration)), pos, minecartEntity)) {
                    return switchConfiguration.getDiverge();
                } else {
                    return straightShape;
                }
            } else if (entrance == switchConfiguration.getDivergentSide()) {
                return divergeShape;
            } else {
                return straightShape;
            }
        } else {
            return shouldDivert(blockReader, pos.offset(this.getMotorDirection(switchConfiguration)), pos, null) ?
                    divergeShape : straightShape;
        }
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    protected RailShape getDivergeShape(SwitchConfiguration switchConfiguration) {
        return switchConfiguration.getDiverge();
    }

    protected abstract RailShape getStraightShape(SwitchConfiguration switchConfiguration);

    protected abstract SwitchConfiguration getSwitchConfiguration(BlockState blockState);

    protected abstract Direction getMotorDirection(SwitchConfiguration switchConfiguration);
}

package xyz.brassgoggledcoders.transport.block.rail.turnout;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.pointmachine.IPointMachineBehavior;
import xyz.brassgoggledcoders.transport.tileentity.rail.SwitchRailTileEntity;

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
            Entity leader = TransportAPI.getConnectionChecker().getLeader(cart);
            if (leader instanceof AbstractMinecartEntity) {
                return motorBehavior.shouldDiverge(motorState, blockReader, motorLocation, switchLocation, (AbstractMinecartEntity) leader);
            } else {
                return motorBehavior.shouldDiverge(motorState, blockReader, motorLocation, switchLocation, cart);
            }
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
    @ParametersAreNonnullByDefault
    public RailShape getRailDirection(BlockState state, IBlockReader blockReader, BlockPos pos,
                                      @Nullable AbstractMinecartEntity minecartEntity) {
        SwitchConfiguration switchConfiguration = getSwitchConfiguration(state);
        RailShape straightShape = getStraightShape(switchConfiguration);
        RailShape divergeShape = getDivergeShape(switchConfiguration);

        if (minecartEntity != null) {
            TileEntity tileEntity = blockReader.getTileEntity(pos);
            if (tileEntity instanceof SwitchRailTileEntity) {
                SwitchRailTileEntity switchRailTileEntity = (SwitchRailTileEntity) tileEntity;
                RailShape cachedRailShape = switchRailTileEntity.getCachedRailShape(minecartEntity);
                if (cachedRailShape == null) {
                    RailShape railShape = this.getSwitchShape(switchConfiguration, straightShape, divergeShape,
                            blockReader, pos, minecartEntity);
                    switchRailTileEntity.setCachedRailShape(minecartEntity, railShape);
                    return railShape;
                } else {
                    return cachedRailShape;
                }
            } else {
                return this.getSwitchShape(switchConfiguration, straightShape, divergeShape, blockReader, pos,
                        minecartEntity);
            }
        } else {
            return shouldDivert(blockReader, pos.offset(this.getMotorDirection(switchConfiguration)), pos, null) ?
                    divergeShape : straightShape;
        }
    }

    public RailShape getSwitchShape(SwitchConfiguration switchConfiguration, RailShape straightShape, RailShape divergeShape,
                                    IBlockReader blockReader, BlockPos pos, AbstractMinecartEntity minecartEntity) {
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
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SwitchRailTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    protected RailShape getDivergeShape(SwitchConfiguration switchConfiguration) {
        return switchConfiguration.getDiverge();
    }

    protected abstract RailShape getStraightShape(SwitchConfiguration switchConfiguration);

    protected abstract SwitchConfiguration getSwitchConfiguration(BlockState blockState);

    protected abstract Direction getMotorDirection(SwitchConfiguration switchConfiguration);
}

package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.block.IEnhancedRail;
import xyz.brassgoggledcoders.transport.blockentity.rail.CachedRailShapeBlockEntity;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.util.DirectionHelper;
import xyz.brassgoggledcoders.transport.util.EnhancedRailState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@SuppressWarnings("deprecation")
public abstract class AbstractSwitchRailBlock extends BaseRailBlock implements EntityBlock, IEnhancedRail {
    public static final BooleanProperty DIVERGE = BooleanProperty.create("diverge");

    protected AbstractSwitchRailBlock(boolean disableCorner, Properties properties) {
        super(disableCorner, properties.randomTicks());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        checkDiverge(pState, pLevel, pPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        checkDiverge(pState, pLevel, pPos);
        pLevel.getBlockEntity(pPos, TransportBlocks.CACHED_RAIL_SHAPE_BLOCK_ENTITY.get())
                .ifPresent(CachedRailShapeBlockEntity::clean);
    }

    public void checkDiverge(BlockState pState, Level pLevel, BlockPos pPos) {
        SwitchConfiguration configuration = this.getSwitchConfiguration(pState);
        Direction powerSide = this.getMotorDirection(configuration);
        int blockSignal = pLevel.getSignal(pPos.relative(powerSide), powerSide);

        if (blockSignal > 0 != pState.getValue(DIVERGE)) {
            boolean wait = pLevel.getBlockEntity(pPos) instanceof CachedRailShapeBlockEntity blockEntity &&
                    blockEntity.getLastHitGameTime() + CachedRailShapeBlockEntity.CACHED_TIME > pLevel.getGameTime();

            if (wait) {
                pLevel.scheduleTick(pPos, this, 5);
            } else {
                pLevel.setBlock(pPos, pState.setValue(DIVERGE, blockSignal > 0), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public RailShape getRailDirection(BlockState state, BlockGetter blockReader, BlockPos pos,
                                      @Nullable AbstractMinecart minecartEntity) {
        SwitchConfiguration switchConfiguration = getSwitchConfiguration(state);
        RailShape straightShape = getStraightShape(switchConfiguration);
        RailShape divergeShape = getDivergeShape(switchConfiguration);

        if (minecartEntity != null && blockReader.getBlockEntity(pos) instanceof CachedRailShapeBlockEntity cachedRailShapeBlockEntity) {

            RailShape railShape = cachedRailShapeBlockEntity.getRailShapeFor(minecartEntity);
            if (railShape == null) {
                int distance = pos.distManhattan(minecartEntity.blockPosition());
                Direction entranceDirection = null;
                if (distance == 0) {
                    entranceDirection = DirectionHelper.getClosestVerticalSide(minecartEntity.position());
                } else if (distance == 1) {
                    entranceDirection = Direction.fromNormal(pos.subtract(minecartEntity.blockPosition()));
                }

                if (entranceDirection != null && entranceDirection.getAxis() != Direction.Axis.Y) {
                    if (entranceDirection == switchConfiguration.getDivergentSide()) {
                        railShape = divergeShape;
                    } else if (entranceDirection == switchConfiguration.getNarrowSide()) {
                        railShape = state.getValue(DIVERGE) ? divergeShape : straightShape;
                    } else {
                        railShape = straightShape;
                    }
                }
            }

            if (railShape != null) {
                cachedRailShapeBlockEntity.setRailShapeFor(minecartEntity, railShape);
                return railShape;
            }
        }

        return state.getValue(DIVERGE) ? divergeShape : straightShape;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CachedRailShapeBlockEntity(TransportBlocks.CACHED_RAIL_SHAPE_BLOCK_ENTITY.get(), pPos, pState);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction != null && this.getMotorDirection(this.getSwitchConfiguration(state)).getOpposite() == direction;
    }

    @NotNull
    protected RailShape getDivergeShape(SwitchConfiguration switchConfiguration) {
        return switchConfiguration.getDiverge();
    }

    @NotNull
    protected abstract RailShape getStraightShape(SwitchConfiguration switchConfiguration);

    protected abstract SwitchConfiguration getSwitchConfiguration(BlockState blockState);

    protected abstract Direction getMotorDirection(SwitchConfiguration switchConfiguration);


    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    protected BlockState updateDir(Level pLevel, BlockPos pPos, BlockState pState, boolean pPlacing) {
        if (pLevel.isClientSide) {
            return pState;
        } else {
            RailShape railshape = pState.getValue(this.getShapeProperty());
            return new EnhancedRailState(pLevel, pPos, pState)
                    .place(pLevel.hasNeighborSignal(pPos), pPlacing, railshape)
                    .getState();
        }
    }

    @Override
    public RailShape[] getCurrentRailShapes(BlockState blockState) {
        SwitchConfiguration switchConfiguration = this.getSwitchConfiguration(blockState);
        return new RailShape[]{
                this.getDivergeShape(switchConfiguration),
                this.getStraightShape(switchConfiguration)
        };
    }

    @Override
    public int getMaxConnections() {
        return 3;
    }
}

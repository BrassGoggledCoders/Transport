package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.block.IEnhancedRail;
import xyz.brassgoggledcoders.transport.blockentity.rail.CachedRailShapeBlockEntity;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.util.DirectionHelper;
import xyz.brassgoggledcoders.transport.api.block.EnhancedRailState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@SuppressWarnings("deprecation")
public class DiamondCrossingRailBlock extends BaseRailBlock implements IEnhancedRail, EntityBlock {
    public static final EnumProperty<RailShape> SHAPE = TransportBlockStateProperties.FLAT_STRAIGHT_RAIL_SHAPE;

    public DiamondCrossingRailBlock(Properties properties) {
        super(true, properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, BaseRailBlock.WATERLOGGED);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        pLevel.getBlockEntity(pPos, TransportBlocks.CACHED_RAIL_SHAPE_BLOCK_ENTITY.get())
                .ifPresent(CachedRailShapeBlockEntity::clean);
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public RailShape getRailDirection(BlockState state, BlockGetter blockGetter, BlockPos pos, @Nullable AbstractMinecart minecartEntity) {
        if (minecartEntity != null && blockGetter.getBlockEntity(pos) instanceof CachedRailShapeBlockEntity cachedRailShapeBlockEntity) {
            RailShape railShape = cachedRailShapeBlockEntity.getRailShapeFor(minecartEntity);
            if (railShape == null) {
                int distance = pos.distManhattan(minecartEntity.blockPosition());
                Direction entranceDirection = null;
                if (distance == 0) {
                    entranceDirection = DirectionHelper.getClosestVerticalSide(minecartEntity.position());
                } else if (distance == 1) {
                    entranceDirection = Direction.fromNormal(pos.subtract(minecartEntity.blockPosition()));
                }

                if (entranceDirection == Direction.NORTH || entranceDirection == Direction.SOUTH) {
                    railShape = RailShape.NORTH_SOUTH;
                } else if (entranceDirection == Direction.EAST || entranceDirection == Direction.WEST) {
                    railShape = RailShape.EAST_WEST;
                }
            }

            if (railShape != null) {
                cachedRailShapeBlockEntity.setRailShapeFor(minecartEntity, railShape);
                return railShape;
            }
        }
        return RailShape.NORTH_SOUTH;
    }

    @NotNull
    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    @Override
    public RailShape[] getCurrentRailShapes(BlockState blockState) {
        return new RailShape[] {
                RailShape.NORTH_SOUTH,
                RailShape.EAST_WEST
        };
    }

    @Override
    public int getMaxConnections() {
        return 4;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CachedRailShapeBlockEntity(TransportBlocks.CACHED_RAIL_SHAPE_BLOCK_ENTITY.get(), pPos, pState);
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    protected BlockState updateDir(Level pLevel, BlockPos pPos, BlockState pState, boolean pPlacing) {
        if (pLevel.isClientSide) {
            return pState;
        } else {
            RailShape railshape = pState.getValue(this.getShapeProperty());
            return new EnhancedRailState(pLevel, pPos, pState)
                    .place(false, pPlacing, railshape)
                    .getState();
        }
    }
}

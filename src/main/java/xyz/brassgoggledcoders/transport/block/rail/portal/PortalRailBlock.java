package xyz.brassgoggledcoders.transport.block.rail.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.rail.TransportBlockStateProperties;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.Random;

public class PortalRailBlock extends BaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final EnumProperty<PortalState> PORTAL_STATE = TransportBlockStateProperties.PORTAL_STATE;

    private static final VoxelShape FLAT_X_AXIS_AABB = Shapes.or(
            Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D),
            BaseRailBlock.FLAT_AABB
    );
    private static final VoxelShape FLAT_Z_AXIS_AABB = Shapes.or(
            Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D),
            BaseRailBlock.FLAT_AABB
    );
    private static final VoxelShape HALF_X_AXIS_AABB = Shapes.or(
            Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D),
            BaseRailBlock.HALF_BLOCK_AABB
    );
    private static final VoxelShape HALF_Z_AXIS_AABB = Shapes.or(
            Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D),
            BaseRailBlock.FLAT_AABB
    );

    public PortalRailBlock(Properties pProperties) {
        super(true, pProperties);
        this.registerDefaultState(this.getStateDefinition()
                .any()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(PORTAL_STATE, PortalState.NONE)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SHAPE, PORTAL_STATE, BaseRailBlock.WATERLOGGED);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {
        if (pState.getValue(PORTAL_STATE) != PortalState.NONE) {
            if (pRand.nextInt(100) == 0) {
                pLevel.playLocalSound((double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D,
                        SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, pRand.nextFloat() * 0.4F + 0.8F, false);
            }

            for (int i = 0; i < 4; ++i) {
                double xPos = (double) pPos.getX() + pRand.nextDouble();
                double yPos = (double) pPos.getY() + pRand.nextDouble();
                double zPos = (double) pPos.getZ() + pRand.nextDouble();
                double xSpeed = ((double) pRand.nextFloat() - 0.5D) * 0.5D;
                double ySpeed = ((double) pRand.nextFloat() - 0.5D) * 0.5D;
                double zSpeed = ((double) pRand.nextFloat() - 0.5D) * 0.5D;
                int j = pRand.nextInt(2) * 2 - 1;
                if (!pLevel.getBlockState(pPos.west()).is(this) && !pLevel.getBlockState(pPos.east()).is(this)) {
                    xPos = (double) pPos.getX() + 0.5D + 0.25D * (double) j;
                    xSpeed = pRand.nextFloat() * 2.0F * (float) j;
                } else {
                    zPos = (double) pPos.getZ() + 0.5D + 0.25D * (double) j;
                    zSpeed = pRand.nextFloat() * 2.0F * (float) j;
                }

                pLevel.addParticle(ParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        RailShape railShape = pState.is(this) ? pState.getValue(SHAPE) : null;
        PortalState portalShape = pState.is(this) ? pState.getValue(PORTAL_STATE) : null;
        if (railShape != null && portalShape != null) {
            return switch (portalShape) {
                case X_AXIS -> railShape.isAscending() ? HALF_X_AXIS_AABB : FLAT_X_AXIS_AABB;
                case Z_AXIS -> railShape.isAscending() ? HALF_Z_AXIS_AABB : FLAT_Z_AXIS_AABB;
                case NONE -> railShape.isAscending() ? HALF_BLOCK_AABB : FLAT_AABB;
            };
        } else {
            return FLAT_AABB;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        if (pState.getValue(PORTAL_STATE) == PortalState.NONE && pBlock instanceof FireBlock) {
            if (inPortalDimension(pLevel)) {
                Optional<RailPortalShape> optional = RailPortalShape.findEmptyRailPortalShape(pLevel, pPos,
                        Direction.fromNormal(pPos.subtract(pFromPos)).getAxis());
                //optional = net.minecraftforge.event.ForgeEventFactory.onTrySpawnPortal(pLevel, pPos, optional);
                optional.ifPresent(RailPortalShape::createPortalBlocks);
            }
        }
    }

    private static boolean inPortalDimension(Level pLevel) {
        return pLevel.dimension() == Level.OVERWORLD || pLevel.dimension() == Level.NETHER;
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        Direction.Axis direction$axis = pFacing.getAxis();
        Direction.Axis direction$axis1 = pState.getValue(PORTAL_STATE).getAxis();
        if (direction$axis1 == null) {
            return pState;
        }
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
        return !flag && (!pFacingState.is(this) && !pFacingState.is(TransportBlocks.PORTAL_RAIL_FILLER.get())) && !(new RailPortalShape(pLevel, pCurrentPos, direction$axis1)).isComplete() ?
                pState.setValue(PORTAL_STATE, PortalState.NONE) : pState;
    }

    @Override
    @NotNull
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }
}

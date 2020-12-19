package xyz.brassgoggledcoders.transport.block;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Function3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.loading.IEntityBlockLoading;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnderLoaderBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    private static final EnumMap<Axis, VoxelShape> CHEVRON_SHAPES = Util.make(Maps.newEnumMap(Axis.class),
            map -> {
                VoxelShape center = Block.makeCuboidShape(3.5, 3.5, 3.5, 12.5, 12.5, 12.5);
                map.put(Axis.X, VoxelShapes.or(center, Block.makeCuboidShape(6, 0, 0, 10, 16, 16)));
                map.put(Axis.Y, VoxelShapes.or(center, Block.makeCuboidShape(0, 6, 0, 16, 10, 16)));
                map.put(Axis.Z, VoxelShapes.or(center, Block.makeCuboidShape(0, 0, 6, 16, 16, 10)));
            }
    );

    private final Function3<BlockState, World, BlockPos, Boolean> tryMove;

    public EnderLoaderBlock(Properties properties, Function3<BlockState, World, BlockPos, Boolean> tryMove) {
        super(properties);
        this.setDefaultState(this.getStateContainer()
                .getBaseState()
                .with(TRIGGERED, false)
                .with(FACING, Direction.NORTH)
        );
        this.tryMove = tryMove;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean isPowered = world.isBlockPowered(pos) || world.isBlockPowered(pos.up());
        boolean isTriggered = state.get(TRIGGERED);
        if (isPowered && !isTriggered) {
            world.getPendingBlockTicks().scheduleTick(pos, this, 4);
            world.setBlockState(pos, state.with(TRIGGERED, true), 3);
        } else if (!isPowered && isTriggered) {
            world.setBlockState(pos, state.with(TRIGGERED, false), 3);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        this.tryMove.apply(state, world, pos);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return CHEVRON_SHAPES.get(state.get(FACING).getAxis());
    }

    @SuppressWarnings("deprecation")
    public static boolean tryMoveBlock(BlockState blockState, World world, BlockPos loaderPos) {
        Direction facing = blockState.get(FACING);
        BlockPos finishPos = loaderPos.offset(facing);
        BlockState finishState = world.getBlockState(finishPos);

        if (!finishState.isSolid()) {
            BlockPos movingPos = loaderPos.offset(facing.getOpposite());
            BlockState movingState = world.getBlockState(movingPos);

            Pair<BlockState, TileEntity> movingPair = null;
            IEntityBlockLoading loadingMethod = null;
            Entity attemptedEntity = null;
            if (movingState.isSolid()) {
                movingPair = Pair.of(movingState, world.getTileEntity(movingPos));
            } else {
                List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(movingPos));
                if (entities.isEmpty() && !movingState.isIn(BlockTags.RAILS)) {
                    movingPair = Pair.of(movingState, world.getTileEntity(movingPos));
                } else if (!entities.isEmpty()) {
                    Iterator<Entity> entityIterator = entities.iterator();
                    while (movingPair == null && entityIterator.hasNext()) {
                        Entity entity = entityIterator.next();
                        IEntityBlockLoading entityBlockLoading = TransportAPI.getBlockLoadingRegistry().getBlockLoadingFor(entity);
                        if (entityBlockLoading != null) {
                            movingPair = entityBlockLoading.attemptUnload(entity);
                            loadingMethod = entityBlockLoading;
                            attemptedEntity = entity;
                        }
                    }
                }
            }

            if (movingPair != null) {
                Iterator<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(finishPos)).iterator();
                boolean loaded = false;
                while (!loaded && entities.hasNext()) {
                    Entity entity = entities.next();
                    IEntityBlockLoading entityBlockLoading = TransportAPI.getBlockLoadingRegistry().getBlockLoadingFor(entity);
                    if (entityBlockLoading != null) {
                        if (entityBlockLoading.attemptLoad(entity, movingPair.getLeft(), movingPair.getRight())) {
                            loaded = true;
                        }
                    }
                }

                if (!loaded && finishState.isAir()) {
                    world.setBlockState(finishPos, movingPair.getLeft());
                    world.setTileEntity(finishPos, movingPair.getRight());
                    loaded = true;
                }

                if (loaded) {
                    if (attemptedEntity != null) {
                        loadingMethod.unload(attemptedEntity);
                    } else {
                        world.removeBlock(movingPos, false);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}

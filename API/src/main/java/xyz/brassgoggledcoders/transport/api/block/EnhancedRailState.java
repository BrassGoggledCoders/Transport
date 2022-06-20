package xyz.brassgoggledcoders.transport.api.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

import javax.annotation.Nullable;
import java.util.List;

public class EnhancedRailState {
    private final Level level;
    private final BlockPos pos;
    private final BaseRailBlock block;
    private final IEnhancedRail enhancedRail;
    private BlockState state;
    private final boolean isStraight;
    private final List<BlockPos> connections = Lists.newArrayList();
    private final boolean canMakeSlopes;

    public EnhancedRailState(Level pLevel, BlockPos pPos, BlockState pState) {
        this.level = pLevel;
        this.pos = pPos;
        this.state = pState;
        this.block = (BaseRailBlock) pState.getBlock();
        RailShape[] railShapes;
        if (this.block instanceof IEnhancedRail enhancedRail) {
            this.enhancedRail = enhancedRail;
            railShapes = enhancedRail.getCurrentRailShapes(pState);
        } else {
            this.enhancedRail = null;
            railShapes = new RailShape[]{
                    this.block.getRailDirection(state, pLevel, pPos, null)
            };
        }
        this.isStraight = !this.block.isFlexibleRail(state, pLevel, pPos);
        this.canMakeSlopes = this.block.canMakeSlopes(state, pLevel, pPos);
        this.updateConnections(railShapes);
    }

    //Used By Detector Rail Block to power connections
    @SuppressWarnings("unused")
    public List<BlockPos> getConnections() {
        return this.connections;
    }

    private void updateConnections(RailShape[] railShapes) {
        this.connections.clear();
        for (RailShape railShape : railShapes) {
            switch (railShape) {
                case NORTH_SOUTH -> {
                    this.connections.add(this.pos.north());
                    this.connections.add(this.pos.south());
                }
                case EAST_WEST -> {
                    this.connections.add(this.pos.west());
                    this.connections.add(this.pos.east());
                }
                case ASCENDING_EAST -> {
                    this.connections.add(this.pos.west());
                    this.connections.add(this.pos.east().above());
                }
                case ASCENDING_WEST -> {
                    this.connections.add(this.pos.west().above());
                    this.connections.add(this.pos.east());
                }
                case ASCENDING_NORTH -> {
                    this.connections.add(this.pos.north().above());
                    this.connections.add(this.pos.south());
                }
                case ASCENDING_SOUTH -> {
                    this.connections.add(this.pos.north());
                    this.connections.add(this.pos.south().above());
                }
                case SOUTH_EAST -> {
                    this.connections.add(this.pos.east());
                    this.connections.add(this.pos.south());
                }
                case SOUTH_WEST -> {
                    this.connections.add(this.pos.west());
                    this.connections.add(this.pos.south());
                }
                case NORTH_WEST -> {
                    this.connections.add(this.pos.west());
                    this.connections.add(this.pos.north());
                }
                case NORTH_EAST -> {
                    this.connections.add(this.pos.east());
                    this.connections.add(this.pos.north());
                }
            }
        }

    }

    private void removeSoftConnections() {
        for (int i = 0; i < this.connections.size(); ++i) {
            EnhancedRailState EnhancedRailState = this.getRail(this.connections.get(i));
            if (EnhancedRailState != null && EnhancedRailState.connectsTo(this)) {
                this.connections.set(i, EnhancedRailState.pos);
            } else {
                this.connections.remove(i--);
            }
        }

    }

    private boolean hasRail(BlockPos pPos) {
        return BaseRailBlock.isRail(this.level, pPos) || BaseRailBlock.isRail(this.level, pPos.above()) || BaseRailBlock.isRail(this.level, pPos.below());
    }

    @Nullable
    private EnhancedRailState getRail(BlockPos pPos) {
        BlockState blockstate = this.level.getBlockState(pPos);
        if (BaseRailBlock.isRail(blockstate)) {
            return new EnhancedRailState(this.level, pPos, blockstate);
        } else {
            BlockPos $$1 = pPos.above();
            blockstate = this.level.getBlockState($$1);
            if (BaseRailBlock.isRail(blockstate)) {
                return new EnhancedRailState(this.level, $$1, blockstate);
            } else {
                $$1 = pPos.below();
                blockstate = this.level.getBlockState($$1);
                return BaseRailBlock.isRail(blockstate) ? new EnhancedRailState(this.level, $$1, blockstate) : null;
            }
        }
    }

    private boolean connectsTo(EnhancedRailState pState) {
        return this.hasConnection(pState.pos);
    }

    private boolean hasConnection(BlockPos pPos) {
        for (BlockPos blockpos : this.connections) {
            if (blockpos.getX() == pPos.getX() && blockpos.getZ() == pPos.getZ()) {
                return true;
            }
        }

        return false;
    }

    //Used By Rail Block to determine if they can form a Junction
    @SuppressWarnings("unused")
    protected int countPotentialConnections() {
        int i = 0;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.hasRail(this.pos.relative(direction))) {
                ++i;
            }
        }

        return i;
    }

    private boolean canConnectTo(EnhancedRailState pState) {
        return this.connectsTo(pState) || this.connections.size() != this.getMaxConnections();
    }

    public int getMaxConnections() {
        if (this.enhancedRail != null) {
            return this.enhancedRail.getMaxConnections();
        } else {
            return 2;
        }
    }

    @SuppressWarnings("deprecation")
    private void connectTo(EnhancedRailState pState) {
        this.connections.add(pState.pos);
        BlockPos northPos = this.pos.north();
        BlockPos southPos = this.pos.south();
        BlockPos westPos = this.pos.west();
        BlockPos eastPos = this.pos.east();
        boolean northConnection = this.hasConnection(northPos);
        boolean southConnection = this.hasConnection(southPos);
        boolean westConnection = this.hasConnection(westPos);
        boolean eastConnection = this.hasConnection(eastPos);
        RailShape railShape = null;
        if (northConnection || southConnection) {
            railShape = RailShape.NORTH_SOUTH;
        }

        if (westConnection || eastConnection) {
            railShape = RailShape.EAST_WEST;
        }

        if (!this.isStraight) {
            if (southConnection && eastConnection && !northConnection && !westConnection) {
                railShape = RailShape.SOUTH_EAST;
            }

            if (southConnection && westConnection && !northConnection && !eastConnection) {
                railShape = RailShape.SOUTH_WEST;
            }

            if (northConnection && westConnection && !southConnection && !eastConnection) {
                railShape = RailShape.NORTH_WEST;
            }

            if (northConnection && eastConnection && !southConnection && !westConnection) {
                railShape = RailShape.NORTH_EAST;
            }
        }

        if (railShape == RailShape.NORTH_SOUTH && canMakeSlopes) {
            if (BaseRailBlock.isRail(this.level, northPos.above())) {
                railShape = RailShape.ASCENDING_NORTH;
            }

            if (BaseRailBlock.isRail(this.level, southPos.above())) {
                railShape = RailShape.ASCENDING_SOUTH;
            }
        }

        if (railShape == RailShape.EAST_WEST && canMakeSlopes) {
            if (BaseRailBlock.isRail(this.level, eastPos.above())) {
                railShape = RailShape.ASCENDING_EAST;
            }

            if (BaseRailBlock.isRail(this.level, westPos.above())) {
                railShape = RailShape.ASCENDING_WEST;
            }
        }

        if (railShape == null) {
            railShape = RailShape.NORTH_SOUTH;
        }

        if (!this.block.isValidRailShape(railShape)) { // Forge: allow rail block to decide if the new shape is valid
            this.connections.remove(pState.pos);
            return;
        }
        //Reason for Deprecation
        this.state = this.state.setValue(this.block.getShapeProperty(), railShape);
        this.level.setBlock(this.pos, this.state, 3);
    }

    private boolean hasNeighborRail(BlockPos pPos) {
        EnhancedRailState EnhancedRailState = this.getRail(pPos);
        if (EnhancedRailState == null) {
            return false;
        } else {
            EnhancedRailState.removeSoftConnections();
            return EnhancedRailState.canConnectTo(this);
        }
    }

    @SuppressWarnings({"ConstantConditions", "deprecation"})
    public EnhancedRailState place(boolean pPowered, boolean pPlaceBlock, RailShape pShape) {
        BlockPos northPos = this.pos.north();
        BlockPos southPos = this.pos.south();
        BlockPos westPos = this.pos.west();
        BlockPos eastPos = this.pos.east();
        boolean northNeighbor = this.hasNeighborRail(northPos);
        boolean southNeighbor = this.hasNeighborRail(southPos);
        boolean westNeighbor = this.hasNeighborRail(westPos);
        boolean eastNeighbor = this.hasNeighborRail(eastPos);
        RailShape railShape = null;
        boolean zAxisNeighbor = northNeighbor || southNeighbor;
        boolean xAxisNeighbor = westNeighbor || eastNeighbor;

        if (zAxisNeighbor && !xAxisNeighbor) {
            railShape = RailShape.NORTH_SOUTH;
        }

        if (xAxisNeighbor && !zAxisNeighbor) {
            railShape = RailShape.EAST_WEST;
        }

        boolean southEastNeighbor = southNeighbor && eastNeighbor;
        boolean southWestNeighbor = southNeighbor && westNeighbor;
        boolean northEastNeighbor = northNeighbor && eastNeighbor;
        boolean northWestNeighbor = northNeighbor && westNeighbor;

        if (!this.isStraight) {
            if (southEastNeighbor && !northNeighbor && !westNeighbor) {
                railShape = RailShape.SOUTH_EAST;
            }

            if (southWestNeighbor && !northNeighbor && !eastNeighbor) {
                railShape = RailShape.SOUTH_WEST;
            }

            if (northWestNeighbor && !southNeighbor && !eastNeighbor) {
                railShape = RailShape.NORTH_WEST;
            }

            if (northEastNeighbor && !southNeighbor && !westNeighbor) {
                railShape = RailShape.NORTH_EAST;
            }
        }

        if (railShape == null) {
            //Reason for Suppress of Constant Conditions???
            if (zAxisNeighbor && xAxisNeighbor) {
                railShape = pShape;
            } else if (zAxisNeighbor) {
                railShape = RailShape.NORTH_SOUTH;
            } else if (xAxisNeighbor) {
                railShape = RailShape.EAST_WEST;
            }

            if (!this.isStraight) {
                if (pPowered) {
                    if (southEastNeighbor) {
                        railShape = RailShape.SOUTH_EAST;
                    }

                    if (southWestNeighbor) {
                        railShape = RailShape.SOUTH_WEST;
                    }

                    if (northEastNeighbor) {
                        railShape = RailShape.NORTH_EAST;
                    }

                    if (northWestNeighbor) {
                        railShape = RailShape.NORTH_WEST;
                    }
                } else {
                    if (northWestNeighbor) {
                        railShape = RailShape.NORTH_WEST;
                    }

                    if (northEastNeighbor) {
                        railShape = RailShape.NORTH_EAST;
                    }

                    if (southWestNeighbor) {
                        railShape = RailShape.SOUTH_WEST;
                    }

                    if (southEastNeighbor) {
                        railShape = RailShape.SOUTH_EAST;
                    }
                }
            }
        }

        if (railShape == RailShape.NORTH_SOUTH && canMakeSlopes) {
            if (BaseRailBlock.isRail(this.level, northPos.above())) {
                railShape = RailShape.ASCENDING_NORTH;
            }

            if (BaseRailBlock.isRail(this.level, southPos.above())) {
                railShape = RailShape.ASCENDING_SOUTH;
            }
        }

        if (railShape == RailShape.EAST_WEST && canMakeSlopes) {
            if (BaseRailBlock.isRail(this.level, eastPos.above())) {
                railShape = RailShape.ASCENDING_EAST;
            }

            if (BaseRailBlock.isRail(this.level, westPos.above())) {
                railShape = RailShape.ASCENDING_WEST;
            }
        }

        if (railShape == null || !this.block.isValidRailShape(railShape)) { // Forge: allow rail block to decide if the new shape is valid
            railShape = pShape;
        }

        this.state = this.state.setValue(this.block.getShapeProperty(), railShape);

        RailShape[] railShapes;
        if (enhancedRail == null) {
            railShapes = new RailShape[]{
                    railShape
            };
        } else {
            railShapes = enhancedRail.getCurrentRailShapes(this.state);
        }
        this.updateConnections(railShapes);

        if (pPlaceBlock || this.level.getBlockState(this.pos) != this.state) {
            this.level.setBlock(this.pos, this.state, 3);

            for (BlockPos connection : this.connections) {
                EnhancedRailState EnhancedRailState = this.getRail(connection);
                if (EnhancedRailState != null) {
                    EnhancedRailState.removeSoftConnections();
                    if (EnhancedRailState.canConnectTo(this)) {
                        EnhancedRailState.connectTo(this);
                    }
                }
            }
        }

        return this;
    }

    public BlockState getState() {
        return this.state;
    }
}

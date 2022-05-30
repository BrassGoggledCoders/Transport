package xyz.brassgoggledcoders.transport.block.rail.portal;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeBlockState;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class RailPortalShape {
    private static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;
    private static final BlockBehaviour.StatePredicate FRAME = IForgeBlockState::isPortalFrame;
    private final LevelAccessor level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private final int width;

    public static Optional<RailPortalShape> findEmptyRailPortalShape(LevelAccessor levelAccessor, BlockPos pPos, Direction.Axis pAxis) {
        return findRailPortalShape(
                levelAccessor,
                pPos,
                (portalShape) -> portalShape.isValid() && portalShape.numPortalBlocks == 0,
                pAxis
        );
    }

    public static Optional<RailPortalShape> findRailPortalShape(LevelAccessor p_77713_, BlockPos p_77714_, Predicate<RailPortalShape> p_77715_, Direction.Axis p_77716_) {
        Optional<RailPortalShape> optional = Optional.of(new RailPortalShape(p_77713_, p_77714_, p_77716_)).filter(p_77715_);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis direction$axis = p_77716_ == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(new RailPortalShape(p_77713_, p_77714_, direction$axis)).filter(p_77715_);
        }
    }

    public RailPortalShape(LevelAccessor p_77695_, @NotNull BlockPos p_77696_, Direction.Axis p_77697_) {
        this.level = p_77695_;
        this.axis = p_77697_;
        this.rightDir = p_77697_ == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(p_77696_);
        if (this.bottomLeft == null) {
            this.bottomLeft = p_77696_;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }

    }

    @Nullable
    private BlockPos calculateBottomLeft(BlockPos pPos) {
        int i = Math.max(this.level.getMinBuildHeight(), pPos.getY() - 21);
        while (pPos.getY() > i && isEmpty(this.level.getBlockState(pPos.below()))) {
            pPos = pPos.below();
        }

        Direction direction = this.rightDir.getOpposite();
        int j = this.getDistanceUntilEdgeAboveFrame(pPos, direction) - 1;
        return j < 0 ? null : pPos.relative(direction, j);
    }

    private int calculateWidth() {
        int i = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        return i >= 2 && i <= 21 ? i : 0;
    }

    private int getDistanceUntilEdgeAboveFrame(BlockPos pPos, Direction pDirection) {
        BlockPos.MutableBlockPos edgeCheckPos = new BlockPos.MutableBlockPos();

        for (int i = 0; i <= 21; ++i) {
            edgeCheckPos.set(pPos)
                    .move(pDirection, i);
            BlockState edgeCheckState = this.level.getBlockState(edgeCheckPos);
            if (!isEmpty(edgeCheckState)) {
                if (FRAME.test(edgeCheckState, this.level, edgeCheckPos)) {
                    return i;
                }
                break;
            }

            BlockState downEdgeCheck = this.level.getBlockState(edgeCheckPos.move(Direction.DOWN));
            if (!FRAME.test(downEdgeCheck, this.level, edgeCheckPos)) {
                break;
            }
        }

        return 0;
    }

    private int calculateHeight() {
        BlockPos.MutableBlockPos topCheckPos = new BlockPos.MutableBlockPos();
        int i = this.getDistanceUntilTop(topCheckPos);
        return i >= MIN_HEIGHT && i <= MAX_HEIGHT && this.hasTopFrame(topCheckPos, i) ? i : 0;
    }

    private boolean hasTopFrame(BlockPos.MutableBlockPos p_77731_, int p_77732_) {
        for (int i = 0; i < this.width; ++i) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = p_77731_.set(this.bottomLeft).move(Direction.UP, p_77732_).move(this.rightDir, i);
            if (!FRAME.test(this.level.getBlockState(blockpos$mutableblockpos), this.level, blockpos$mutableblockpos)) {
                return false;
            }
        }

        return true;
    }

    private int getDistanceUntilTop(BlockPos.MutableBlockPos p_77729_) {
        for (int i = 0; i < 21; ++i) {
            p_77729_.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!FRAME.test(this.level.getBlockState(p_77729_), this.level, p_77729_)) {
                return i;
            }

            p_77729_.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!FRAME.test(this.level.getBlockState(p_77729_), this.level, p_77729_)) {
                return i;
            }

            for (int j = 0; j < this.width; ++j) {
                p_77729_.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState blockstate = this.level.getBlockState(p_77729_);
                if (!isEmpty(blockstate)) {
                    return i;
                }

                if (blockstate.is(TransportBlocks.PORTAL_RAIL_FILLER.get()) ||
                        (blockstate.is(TransportBlocks.PORTAL_RAIL_BLOCK.get()) && blockstate.getValue(PortalRailBlock.PORTAL_STATE) != PortalState.NONE)) {
                    ++this.numPortalBlocks;
                }
            }
        }

        return 21;
    }

    private static boolean isEmpty(BlockState pState) {
        return pState.isAir() || pState.is(BlockTags.FIRE) || (pState.is(TransportBlocks.PORTAL_RAIL_BLOCK.get()) &&
                pState.getValue(PortalRailBlock.PORTAL_STATE) == PortalState.NONE) ||
                pState.is(TransportBlocks.PORTAL_RAIL_FILLER.get());
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void createPortalBlocks() {
        BlockState blockstate = TransportBlocks.PORTAL_RAIL_FILLER.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1))
                .forEach((p_77725_) -> {
                    if (p_77725_.getY() == bottomLeft.getY()) {
                        BlockState blockState = this.level.getBlockState(p_77725_);
                        if (blockState.is(TransportBlocks.PORTAL_RAIL_BLOCK.get())) {
                            this.level.setBlock(p_77725_, blockState.setValue(PortalRailBlock.PORTAL_STATE, PortalState.fromAxis(this.axis)), 18);
                        } else {
                            this.level.setBlock(p_77725_, blockstate, 18);
                        }
                    } else {
                        this.level.setBlock(p_77725_, blockstate, 18);
                    }
                });
    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }

    public static Vec3 getRelativePosition(BlockUtil.FoundRectangle p_77739_, Direction.Axis p_77740_, Vec3 p_77741_, EntityDimensions p_77742_) {
        double d0 = (double) p_77739_.axis1Size - (double) p_77742_.width;
        double d1 = (double) p_77739_.axis2Size - (double) p_77742_.height;
        BlockPos blockpos = p_77739_.minCorner;
        double d2;
        if (d0 > 0.0D) {
            float f = (float) blockpos.get(p_77740_) + p_77742_.width / 2.0F;
            d2 = Mth.clamp(Mth.inverseLerp(p_77741_.get(p_77740_) - (double) f, 0.0D, d0), 0.0D, 1.0D);
        } else {
            d2 = 0.5D;
        }

        double d4;
        if (d1 > 0.0D) {
            Direction.Axis direction$axis = Direction.Axis.Y;
            d4 = Mth.clamp(Mth.inverseLerp(p_77741_.get(direction$axis) - (double) blockpos.get(direction$axis), 0.0D, d1), 0.0D, 1.0D);
        } else {
            d4 = 0.0D;
        }

        Direction.Axis direction$axis1 = p_77740_ == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        double d3 = p_77741_.get(direction$axis1) - ((double) blockpos.get(direction$axis1) + 0.5D);
        return new Vec3(d2, d4, d3);
    }

    public static PortalInfo createPortalInfo(ServerLevel p_77700_, BlockUtil.FoundRectangle p_77701_, Direction.Axis p_77702_, Vec3 p_77703_, EntityDimensions p_77704_, Vec3 p_77705_, float p_77706_, float p_77707_) {
        BlockPos blockpos = p_77701_.minCorner;
        BlockState blockstate = p_77700_.getBlockState(blockpos);
        Direction.Axis direction$axis = blockstate.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double d0 = (double) p_77701_.axis1Size;
        double d1 = (double) p_77701_.axis2Size;
        int i = p_77702_ == direction$axis ? 0 : 90;
        Vec3 vec3 = p_77702_ == direction$axis ? p_77705_ : new Vec3(p_77705_.z, p_77705_.y, -p_77705_.x);
        double d2 = (double) p_77704_.width / 2.0D + (d0 - (double) p_77704_.width) * p_77703_.x();
        double d3 = (d1 - (double) p_77704_.height) * p_77703_.y();
        double d4 = 0.5D + p_77703_.z();
        boolean flag = direction$axis == Direction.Axis.X;
        Vec3 vec31 = new Vec3((double) blockpos.getX() + (flag ? d2 : d4), (double) blockpos.getY() + d3, (double) blockpos.getZ() + (flag ? d4 : d2));
        return new PortalInfo(vec31, vec3, p_77706_ + (float) i, p_77707_);
    }
}

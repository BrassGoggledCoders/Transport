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
import xyz.brassgoggledcoders.transport.blockentity.rail.SwitchRailBlockEntity;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public abstract class AbstractSwitchRailBlock extends BaseRailBlock implements EntityBlock {
    public static final BooleanProperty DIVERGE = BooleanProperty.create("diverge");

    protected AbstractSwitchRailBlock(boolean disableCorner, Properties properties) {
        super(disableCorner, properties);
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
    }

    public void checkDiverge(BlockState pState, Level pLevel, BlockPos pPos) {
        SwitchConfiguration configuration = this.getSwitchConfiguration(pState);
        Direction powerSide = this.getMotorDirection(configuration);
        int blockSignal = pLevel.getSignal(pPos.relative(powerSide), powerSide);

        if (blockSignal > 0 != pState.getValue(DIVERGE)) {
            boolean wait = pLevel.getBlockEntity(pPos) instanceof SwitchRailBlockEntity blockEntity &&
                    blockEntity.getLastHitGameTime() + 5 > pLevel.getGameTime();

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
        return new SwitchRailBlockEntity(TransportBlocks.SWITCH_RAIL_BLOCK_ENTITY.get(), pPos, pState);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction != null && this.getMotorDirection(this.getSwitchConfiguration(state)).getOpposite() == direction;
    }

    protected RailShape getDivergeShape(SwitchConfiguration switchConfiguration) {
        return switchConfiguration.getDiverge();
    }

    protected abstract RailShape getStraightShape(SwitchConfiguration switchConfiguration);

    protected abstract SwitchConfiguration getSwitchConfiguration(BlockState blockState);

    protected abstract Direction getMotorDirection(SwitchConfiguration switchConfiguration);


}

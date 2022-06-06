package xyz.brassgoggledcoders.transport.block.rail.signal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.block.rail.TransportBlockStateProperties;
import xyz.brassgoggledcoders.transport.blockentity.rail.OneWaySignalRailBlockEntity;
import xyz.brassgoggledcoders.transport.signal.SignalBlock;
import xyz.brassgoggledcoders.transport.signal.SignalLevelData;
import xyz.brassgoggledcoders.transport.util.MinecartHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@SuppressWarnings("deprecation")
public class OneWaySignalRailBlock extends BaseRailBlock implements EntityBlock {
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final EnumProperty<SignalState> SIGNAL = TransportBlockStateProperties.SIGNAL_STATE;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    public OneWaySignalRailBlock(Properties pProperties) {
        super(true, pProperties);
        this.registerDefaultState(this.getStateDefinition()
                .any()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(SIGNAL, SignalState.SLOW)
                .setValue(INVERTED, false)
                .setValue(BaseRailBlock.WATERLOGGED, false)
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        SignalState signalState = pState.getValue(SIGNAL);
        SignalState forwardPointState = pLevel.getBlockEntity(pPos, OneWaySignalRailBlockEntity.TYPE.get())
                .map(blockEntity -> {
                    SignalBlock block = blockEntity.getForwardBlock();
                    if (block == SignalBlock.EMPTY) {
                        return SignalState.SLOW;
                    } else {
                        return block.isOccupied() ? SignalState.STOP : SignalState.PROCEED;
                    }
                })
                .orElse(SignalState.SLOW);

        if (signalState != forwardPointState) {
            pLevel.setBlock(pPos, pState.setValue(SIGNAL, forwardPointState), Block.UPDATE_ALL);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (state.getValue(SIGNAL).isPowered()) {
            MinecartHelper.boostMinecart(state, pos, SHAPE, cart);
        }
        SignalLevelData.getFor(level)
                .ifPresent(signalLevelData -> signalLevelData.setMinecartOnSignalPoint(cart, pos));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        SignalLevelData.getFor(pLevel)
                .ifPresent(signalLevelData -> signalLevelData.removeSignalPoint(pPos));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SHAPE, SIGNAL, INVERTED, BaseRailBlock.WATERLOGGED);
    }

    @Override
    @NotNull
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new OneWaySignalRailBlockEntity(pPos, pState);
    }
}

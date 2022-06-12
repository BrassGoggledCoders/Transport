package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class InvertedPoweredRailBlock extends PoweredRailBlock {
    public InvertedPoweredRailBlock(Properties builder) {
        super(builder, true);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(POWERED, true)
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void updateState(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock) {
        boolean poweredState = pState.getValue(POWERED);
        boolean neighborsPowered = pLevel.hasNeighborSignal(pPos) ||
                this.findPoweredRailSignal(pLevel, pPos, pState, true, 0) ||
                this.findPoweredRailSignal(pLevel, pPos, pState, false, 0);
        if (neighborsPowered == poweredState) {
            pLevel.setBlock(pPos, pState.setValue(POWERED, !neighborsPowered), 3);
            pLevel.updateNeighborsAt(pPos.below(), this);
            if (pState.getValue(SHAPE).isAscending()) {
                pLevel.updateNeighborsAt(pPos.above(), this);
            }
        }

    }
}

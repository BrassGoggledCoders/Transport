package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.content.TransportWeathering;

import java.util.Optional;

public class WaxedCopperRail extends RailBlock {
    public WaxedCopperRail(Properties properties) {
        super(properties);
    }

    @Override
    public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        return super.getRailMaxSpeed(state, level, pos, cart) / 2;
    }

    @Override
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (state.getValue(this.getShapeProperty()).isAscending() &&
                level.getBlockState(pos.above()).isFaceSturdy(level, pos, Direction.DOWN, SupportType.RIGID)) {
            cart.setDeltaMovement(Vec3.ZERO);
        } else {
            cart.setDeltaMovement(cart.getDeltaMovement().scale(1.2));
        }
    }

    @Override
    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_WAX_OFF) {
            Optional<BlockState> blockState = Optional.ofNullable(
                            TransportWeathering.WAX_OFF.get()
                                    .get(state.getBlock())
                    )
                    .map(block -> block.withPropertiesOf(state));

            if (blockState.isPresent()) {
                return blockState.get();
            }
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}

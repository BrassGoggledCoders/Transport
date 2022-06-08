package xyz.brassgoggledcoders.transport.api.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.jetbrains.annotations.Nullable;

public interface IEnhancedRail {
    RailShape[] getCurrentRailShapes(BlockState blockState);

    @Nullable
    RailShape getRailShapeForDirection(Direction direction, BlockState blockState);

    int getMaxConnections();

}

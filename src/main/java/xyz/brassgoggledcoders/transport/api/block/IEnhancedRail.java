package xyz.brassgoggledcoders.transport.api.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public interface IEnhancedRail {
    RailShape[] getCurrentRailShapes(BlockState blockState);

    int getMaxConnections();

}

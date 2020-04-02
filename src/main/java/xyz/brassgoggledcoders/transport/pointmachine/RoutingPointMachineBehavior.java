package xyz.brassgoggledcoders.transport.pointmachine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.pointmachine.IPointMachineBehavior;
import xyz.brassgoggledcoders.transport.api.routing.RoutingStorage;

import javax.annotation.Nullable;

public class RoutingPointMachineBehavior implements IPointMachineBehavior {
    @Override
    public boolean shouldDiverge(BlockState motorState, IBlockReader blockReader, BlockPos motorPos, BlockPos switchPos,
                                 @Nullable AbstractMinecartEntity minecartEntity) {
        if (minecartEntity != null) {
            if (motorPos.offset(motorState.get(BlockStateProperties.HORIZONTAL_FACING).getOpposite()).equals(switchPos)) {
                TileEntity tileEntity = blockReader.getTileEntity(motorPos);
                if (tileEntity != null) {
                    return tileEntity.getCapability(TransportAPI.ROUTING_STORAGE)
                            .map(routingStorage -> routingStorage.getRouting(tileEntity))
                            .map(routing -> routing.matches(minecartEntity))
                            .orElse(false);
                }
            }
        }
        return false;
    }

    @Override
    public void onBlockStateUpdate(BlockState motorState, IBlockReader blockReader, BlockPos motorPos) {
        TileEntity tileEntity = blockReader.getTileEntity(motorPos);
        if (tileEntity != null) {
            tileEntity.getCapability(TransportAPI.ROUTING_STORAGE).ifPresent(RoutingStorage::invalidate);
        }
    }
}

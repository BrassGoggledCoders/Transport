package xyz.brassgoggledcoders.transport.pointmachine;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.pointmachine.IPointMachineBehavior;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateStorage;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class PredicatePointMachineBehavior implements IPointMachineBehavior {
    @Override
    public boolean shouldDiverge(BlockState motorState, IBlockReader blockReader, BlockPos motorPos, BlockPos switchPos,
                                 @Nullable AbstractMinecartEntity minecartEntity) {
        if (minecartEntity != null) {
            if (motorPos.offset(motorState.get(BlockStateProperties.HORIZONTAL_FACING).getOpposite()).equals(switchPos)) {
                TileEntity tileEntity = blockReader.getTileEntity(motorPos);
                if (tileEntity != null) {
                    return tileEntity.getCapability(TransportAPI.PREDICATE_STORAGE)
                            .map(predicateStorage -> predicateStorage.getPredicate(tileEntity))
                            .map(routing -> this.handleRouting(routing, minecartEntity))
                            .orElse(false);
                }
            }
        }
        return false;
    }

    private Boolean handleRouting(Either<String, Predicate<Entity>> routing, AbstractMinecartEntity minecartEntity) {
        return routing.ifLeft(error -> {
            for (Entity entity : minecartEntity.getPassengers()) {
                if (entity instanceof PlayerEntity && !entity.getEntityWorld().isRemote()) {
                    ((PlayerEntity) entity).sendStatusMessage(new StringTextComponent(error), true);
                }
            }
        }).mapRight(value -> value.test(minecartEntity))
                .right()
                .orElse(false);
    }

    @Override
    public void onBlockStateUpdate(BlockState motorState, IBlockReader blockReader, BlockPos motorPos) {
        TileEntity tileEntity = blockReader.getTileEntity(motorPos);
        if (tileEntity != null) {
            tileEntity.getCapability(TransportAPI.PREDICATE_STORAGE).ifPresent(PredicateStorage::invalidate);
        }
    }
}

package xyz.brassgoggledcoders.transport.blockentity;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.block.DumpRailBlock;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

public class DumpRailBlockEntity extends BlockEntity {
    private final Map<UUID, Tuple<Long, Boolean>> actedOn;

    public DumpRailBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.actedOn = Maps.newHashMap();
    }

    public <T> void tryDump(AbstractMinecart cart, Capability<T> capability, BiConsumer<T, T> transfer) {
        if (this.getLevel() != null) {
            long gameTime = this.getLevel().getGameTime();
            actedOn.values().removeIf(tuple -> tuple.getA() + 100 < gameTime);

            if (!actedOn.containsKey(cart.getUUID())) {
                actedOn.put(cart.getUUID(), new Tuple<>(gameTime, false));
                LazyOptional<T> cartOptional = cart.getCapability(capability);
                if (cartOptional.isPresent()) {
                    BlockEntity blockEntity = Objects.requireNonNull(this.getLevel())
                            .getBlockEntity(this.getBlockPos().below());
                    if (blockEntity != null) {
                        LazyOptional<T> blockEntityOptional = blockEntity.getCapability(capability);
                        if (blockEntityOptional.isPresent()) {
                            transfer.accept(
                                    cartOptional.orElseThrow(IllegalStateException::new),
                                    blockEntityOptional.orElseThrow(IllegalStateException::new)
                            );
                        }
                    }
                }
            } else {
                Tuple<Long, Boolean> cartValue = actedOn.get(cart.getUUID());
                if (cartValue != null && !cartValue.getB()) {
                    LazyOptional<T> cartOptional = cart.getCapability(capability);
                    if (cartOptional.isPresent()) {
                        BlockPos startPos;
                        RailShape railShape = this.getBlockState().getValue(DumpRailBlock.RAIL_SHAPE);
                        if (railShape.isAscending()) {
                            startPos = this.getBlockPos().above(2);
                        } else {
                            startPos = this.getBlockPos().above();
                        }

                        BlockEntity blockEntity = this.getLevel().getBlockEntity(startPos);
                        if (blockEntity == null) {
                            blockEntity = this.getLevel().getBlockEntity(startPos.above());
                        }

                        if (blockEntity != null) {
                            LazyOptional<T> blockEntityOptional = blockEntity.getCapability(capability);
                            if (blockEntityOptional.isPresent()) {
                                transfer.accept(
                                        blockEntityOptional.orElseThrow(IllegalStateException::new),
                                        cartOptional.orElseThrow(IllegalStateException::new)
                                );
                            }
                        }
                    }
                }
            }
        }
    }
}

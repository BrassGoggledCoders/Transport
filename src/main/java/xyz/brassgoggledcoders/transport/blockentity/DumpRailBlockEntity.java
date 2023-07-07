package xyz.brassgoggledcoders.transport.blockentity;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Function3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.block.rail.DumpRailBlock;

import java.util.Map;
import java.util.OptionalInt;
import java.util.UUID;

public class DumpRailBlockEntity extends BlockEntity {
    private final Map<UUID, DumpState> dumpStates;

    public DumpRailBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.dumpStates = Maps.newHashMap();
    }

    public <T> void tryDump(AbstractMinecart cart, Capability<T> capability, Function3<T, T, OptionalInt, OptionalInt> transfer) {
        if (this.getLevel() != null) {
            long gameTime = this.getLevel().getGameTime();
            dumpStates.values().removeIf(dumpState -> dumpState.lastSeen() + 40 < gameTime);

            DumpState dumpState = dumpStates.computeIfAbsent(cart.getUUID(), uuid -> new DumpState(gameTime, DumpType.UNLOADING, OptionalInt.empty()));

            if (dumpState.type() != DumpType.DONE) {
                LazyOptional<T> cartOptional = cart.getCapability(capability);

                if (cartOptional.isPresent()) {
                    RailShape railShape = this.getBlockState().getValue(DumpRailBlock.RAIL_SHAPE);
                    LazyOptional<T> blockOptional = dumpState.type.getCapability(level, this.getBlockPos(),
                            railShape.isAscending(), capability);

                    if (blockOptional.isPresent()) {
                        OptionalInt counter = switch (dumpState.type()) {
                            case UNLOADING -> transfer.apply(
                                    cartOptional.orElseThrow(IllegalStateException::new),
                                    blockOptional.orElseThrow(IllegalStateException::new),
                                    dumpState.counter()
                            );
                            case LOADING -> transfer.apply(
                                    blockOptional.orElseThrow(IllegalStateException::new),
                                    cartOptional.orElseThrow(IllegalStateException::new),
                                    dumpState.counter()
                            );
                            default -> throw new IllegalStateException("Unexpected value: " + dumpState.type());
                        };
                        dumpStates.put(cart.getUUID(), new DumpState(
                                gameTime,
                                counter.isPresent() ? dumpState.type() : dumpState.type().getNext(),
                                counter
                        ));
                    } else {
                        dumpStates.put(cart.getUUID(), new DumpState(gameTime, dumpState.type().getNext(), OptionalInt.empty()));
                    }
                } else {
                    dumpStates.put(cart.getUUID(), new DumpState(gameTime, dumpState.type().getNext(), OptionalInt.empty()));
                }
            }
        }
    }

    private enum DumpType {
        UNLOADING {
            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(Level level, BlockPos railPos, boolean ascending, Capability<T> capability) {
                BlockEntity blockEntity = level.getBlockEntity(railPos.below());
                if (blockEntity != null) {
                    return blockEntity.getCapability(capability, Direction.UP);
                }
                return LazyOptional.empty();
            }
        },
        LOADING {
            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(Level level, BlockPos railPos, boolean ascending, Capability<T> capability) {
                BlockEntity blockEntity = level.getBlockEntity(railPos.above(ascending ? 2 : 1));
                if (blockEntity == null) {
                    blockEntity = level.getBlockEntity(railPos.above(ascending ? 3 : 2));
                }
                if (blockEntity != null) {
                    return blockEntity.getCapability(capability, Direction.DOWN);
                }
                return LazyOptional.empty();
            }
        },
        DONE;

        @NotNull
        public <T> LazyOptional<T> getCapability(Level level, BlockPos railPos, boolean ascending, Capability<T> capability) {
            return LazyOptional.empty();
        }

        public DumpType getNext() {
            return switch (this) {
                case UNLOADING -> LOADING;
                case LOADING, DONE -> DONE;
            };
        }
    }

    private record DumpState(long lastSeen, DumpType type, OptionalInt counter) {

    }
}

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
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
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

    public <T> void tryDump(AbstractMinecart cart, EntityCapability<T, Direction> capability, BlockCapability<T, Direction> blockCapability,
                            Function3<T, T, OptionalInt, OptionalInt> transfer) {
        if (this.getLevel() != null) {
            long gameTime = this.getLevel().getGameTime();
            dumpStates.values().removeIf(dumpState -> dumpState.lastSeen() + 40 < gameTime);

            DumpState dumpState = dumpStates.computeIfAbsent(cart.getUUID(), uuid -> new DumpState(gameTime, DumpType.UNLOADING, OptionalInt.empty()));

            if (dumpState.type() != DumpType.DONE) {
                T cartCap = cart.getCapability(capability, null);

                if (cartCap != null) {
                    RailShape railShape = this.getBlockState().getValue(DumpRailBlock.RAIL_SHAPE);
                    T blockCap = dumpState.type.getCapability(level, this.getBlockPos(),
                            railShape.isAscending(), blockCapability);

                    if (blockCap != null) {
                        OptionalInt counter = switch (dumpState.type()) {
                            case UNLOADING -> transfer.apply(
                                    cartCap,
                                    blockCap,
                                    dumpState.counter()
                            );
                            case LOADING -> transfer.apply(
                                    blockCap,
                                    cartCap,
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
            @Override
            public <T> T getCapability(Level level, BlockPos railPos, boolean ascending, BlockCapability<T, Direction> capability) {
                return level.getCapability(capability, railPos, Direction.UP);
            }
        },
        LOADING {
            @Override
            public <T> T getCapability(Level level, BlockPos railPos, boolean ascending, BlockCapability<T, Direction> capability) {
                T value = level.getCapability(capability, railPos.above(ascending ? 2 : 1), Direction.DOWN);
                if (value == null) {
                    value = level.getCapability(capability, railPos.above(ascending ? 3 : 2), Direction.DOWN);
                }
                return value;
            }
        },
        DONE;

        public <T> T getCapability(Level level, BlockPos railPos, boolean ascending, BlockCapability<T, Direction> capability) {
            return null;
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

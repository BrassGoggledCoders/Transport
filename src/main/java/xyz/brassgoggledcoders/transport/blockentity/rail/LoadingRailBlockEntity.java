package xyz.brassgoggledcoders.transport.blockentity.rail;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import xyz.brassgoggledcoders.transport.block.rail.LoadingRailBlock;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.util.DirectionHelper;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

public class LoadingRailBlockEntity extends BlockEntity {
    private final int UNITS = 16;
    private final Map<UUID, LoadState> loadStates;

    public LoadingRailBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(TransportBlocks.LOADING_RAIL_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        this.loadStates = Maps.newHashMap();
    }

    public <T> void tryLoading(AbstractMinecart cart, boolean loading, EntityCapability<T, Direction> entityCapability,
                               BlockCapability<T, Direction> blockCapability,
                               Function3<T, T, Pair<Integer, Integer>, Pair<Integer, Integer>> transfer
    ) {
        if (this.getLevel() != null) {
            long gameTime = this.getLevel().getGameTime();
            loadStates.values().removeIf(loadState -> loadState.lastSeen() + 40 < gameTime);

            LoadState loadState = loadStates.computeIfAbsent(cart.getUUID(), uuid -> new LoadState(
                    gameTime,
                    false,
                    Optional.empty()
            ));

            if (!loadState.done()) {
                T cartCapabilityValue = cart.getCapability(entityCapability, null);

                if (cartCapabilityValue != null) {
                    RailShape railShape = this.getBlockState().getValue(LoadingRailBlock.RAIL_SHAPE);

                    Pair<Integer, Integer> counter = loadState.lastLoad()
                            .map(lastLoad -> {
                                if (lastLoad.counter().isPresent()) {
                                    return Pair.of(UNITS, lastLoad.counter().getAsInt());
                                } else {
                                    return Pair.of(UNITS, 0);
                                }
                            })
                            .orElseGet(() -> Pair.of(UNITS, 0));

                    Optional<Direction> direction = loadState.lastLoad()
                            .map(LastLoad::direction);
                    if (counter.getSecond() <= 0) {
                        direction = direction.map(DirectionHelper::next);
                    }

                    if (direction.isEmpty()) {
                        direction = Optional.of(Direction.DOWN);
                    }

                    while (counter.getFirst() > 0 && direction.isPresent()) {
                        BlockPos nextCheckPos;
                        if (direction.get() == Direction.UP && railShape.isAscending()) {
                            nextCheckPos = this.getBlockPos().above(2);
                        } else {
                            nextCheckPos = this.getBlockPos().relative(direction.get());
                        }

                        T blockCapabilityValue = this.getLevel().getCapability(blockCapability, nextCheckPos, direction.get().getOpposite());
                        if (blockCapabilityValue == null && direction.get() == Direction.UP) {
                            blockCapabilityValue = this.getLevel().getCapability(blockCapability, nextCheckPos.above(), direction.get().getOpposite());
                        }

                        if (blockCapabilityValue != null) {
                            if (loading) {
                                counter = transfer.apply(
                                        blockCapabilityValue,
                                        cartCapabilityValue,
                                        counter
                                );
                            } else {
                                counter = transfer.apply(
                                        cartCapabilityValue,
                                        blockCapabilityValue,
                                        counter
                                );
                            }
                            if (counter.getSecond() == 0) {
                                direction = direction.flatMap(value -> DirectionHelper.next(value, false));
                            }
                        } else {
                            counter = counter.mapFirst(units -> units - 4);
                            direction = direction.flatMap(value -> DirectionHelper.next(value, false));
                        }
                    }
                    Pair<Integer, Integer> finalCounter = counter;
                    loadStates.put(cart.getUUID(), new LoadState(
                            gameTime,
                            direction.isEmpty(),
                            direction.map(value -> new LastLoad(
                                    value,
                                    OptionalInt.of(finalCounter.getSecond())
                            ))
                    ));
                } else {
                    loadStates.put(cart.getUUID(), new LoadState(gameTime, true, Optional.empty()));
                }
            }
        }
    }

    private record LoadState(long lastSeen, boolean done, Optional<LastLoad> lastLoad) {

    }

    private record LastLoad(Direction direction, OptionalInt counter) {

    }
}

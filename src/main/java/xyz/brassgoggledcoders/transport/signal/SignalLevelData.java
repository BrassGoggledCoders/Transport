package xyz.brassgoggledcoders.transport.signal;

import com.google.common.collect.Maps;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.Transport;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings({"UnstableApiUsage", "UnusedReturnValue"})
public class SignalLevelData extends SavedData {
    public static final String NAME = Transport.ID + ":signals";

    private final ServerLevel serverLevel;
    private final MutableNetwork<SignalPoint, SignalBlock> network;

    private final Map<UUID, SignaledEntity> signaledEntities;

    public SignalLevelData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        this.network = NetworkBuilder.directed()
                .build();
        this.signaledEntities = Maps.newHashMap();
    }

    public SignalLevelData(ServerLevel serverLevel, CompoundTag compoundTag) {
        this(serverLevel);
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        return new CompoundTag();
    }

    public boolean addSignalPoint(SignalPoint signalPoint) {
        return network.addNode(signalPoint);
    }

    public boolean removeSignalPoint(BlockPos signalPos) {
        return this.getSignalPointByBlockPos(signalPos)
                .map(this::removeSignalPoint)
                .orElse(false);
    }

    public boolean removeSignalPoint(SignalPoint signalPoint) {
        return network.removeNode(signalPoint);
    }

    public Optional<SignalPoint> getSignalPointByUUID(UUID uuid) {
        return network.nodes()
                .parallelStream()
                .filter(signalPoint -> signalPoint.uuid().equals(uuid))
                .findFirst();
    }

    public Optional<SignalPoint> getSignalPointByBlockPos(BlockPos blockPos) {
        return network.nodes()
                .parallelStream()
                .filter(signalPoint -> signalPoint.blockPos().equals(blockPos))
                .findFirst();
    }

    public Set<SignalBlock> getForwardSignalBlocks(SignalPoint signalPoint) {
        return network.outEdges(signalPoint);
    }

    public SignalPoint createSignalPoint(Block block, BlockPos blockPos) {
        SignalPoint signalPoint = new SignalPoint(UUID.randomUUID(), block, blockPos);
        if (!this.addSignalPoint(signalPoint)) {
            Transport.LOGGER.warn("Signal Point for {} already exists", blockPos.toString());
        }
        return signalPoint;
    }

    public void tick() {

    }

    @ParametersAreNonnullByDefault
    public void onEntityPass(AbstractMinecart minecart, SignalPoint signalPoint) {
        if (this.signaledEntities.containsKey(minecart.getUUID())) {
            SignaledEntity signaledEntity = this.signaledEntities.get(minecart.getUUID());
            if (!signalPoint.equals(signaledEntity.getLastSignalPoint())) {
                if (signaledEntity.getLastSignalPoint() != null && signaledEntity.getCurrentSignalBlock() == null) {
                    this.createSignalBlock(
                            signaledEntity.getLastSignalPoint(),
                            signalPoint
                    );
                }
                signaledEntity.setLastSignalPoint(signalPoint);
            }
        } else {
            SignaledEntity signaledEntity = new SignaledEntity(minecart.getUUID());
            signaledEntity.setLastSignalPoint(signalPoint);
            this.signaledEntities.put(minecart.getUUID(), signaledEntity);
        }
    }

    @ParametersAreNonnullByDefault
    public boolean createSignalBlock(SignalPoint signalPointU, SignalPoint signalPointV) {
        boolean changed = this.network.addEdge(signalPointU, signalPointV, new SignalBlock(UUID.randomUUID()));
        if (changed) {
            this.serverLevel.scheduleTick(signalPointU.blockPos(), signalPointU.block(), 1);
        }
        return changed;
    }

    @SuppressWarnings("unused")
    public void onChunkUnload(ChunkAccess chunk) {

    }

    public void onEntityLeave(AbstractMinecart minecart) {
        Entity.RemovalReason reason = minecart.getRemovalReason();
        if (reason != null) {
            switch (reason) {
                case KILLED, DISCARDED, CHANGED_DIMENSION -> removeEntity(minecart);
                case UNLOADED_TO_CHUNK, UNLOADED_WITH_PLAYER -> handleUnload(minecart);
            }
        }
    }

    private void removeEntity(AbstractMinecart minecart) {
        SignaledEntity signaledEntity = this.signaledEntities.remove(minecart.getUUID());
        if (signaledEntity != null) {
            Transport.LOGGER.warn("Removed Signaled Entity");
        }
    }

    private void handleUnload(AbstractMinecart minecart) {
        if (this.signaledEntities.containsKey(minecart.getUUID())) {
            Transport.LOGGER.warn("Unloading Signaled Entity");
        }
    }

    public static Optional<SignalLevelData> getFor(@Nullable LevelAccessor level) {
        if (level instanceof ServerLevel serverLevel) {
            return Optional.of(getFor(serverLevel));
        } else {
            return Optional.empty();
        }
    }

    public static SignalLevelData getFor(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(
                        compoundTag -> new SignalLevelData(serverLevel, compoundTag),
                        () -> new SignalLevelData(serverLevel),
                        NAME
                );
    }
}

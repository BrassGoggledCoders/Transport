package xyz.brassgoggledcoders.transport.signal;

import com.google.common.collect.Maps;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import com.mojang.datafixers.util.Pair;
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
import xyz.brassgoggledcoders.transport.util.NBTHelper;

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
        loadData(compoundTag);
    }

    private void loadData(CompoundTag compoundTag) {
        NBTHelper.readNetwork(
                compoundTag.getCompound("Network"),
                this::addSignalPoint,
                this::loadEdge,
                SignalPoint::fromTag,
                SignalBlock::fromTag
        );

        CompoundTag signaledEntitiesTag = compoundTag.getCompound("SignaledEntities");
        for (String key : signaledEntitiesTag.getAllKeys()) {
            this.signaledEntities.put(
                    UUID.fromString(key),
                    SignaledEntity.fromTag(
                            signaledEntitiesTag.getCompound(key),
                            this::getSignalPointByUUID,
                            this::getSignalBlockByUUID
                    )
            );
        }
    }

    private void loadEdge(SignalBlock signalBlock, Pair<UUID, UUID> signalPointUUIDs) {
        Optional<SignalPoint> signalPointU = this.getSignalPointByUUID(signalPointUUIDs.getFirst());
        Optional<SignalPoint> signalPointV = this.getSignalPointByUUID(signalPointUUIDs.getSecond());

        if (signalPointU.isEmpty()) {
            Transport.LOGGER.warn("Failed to find SignalPoint with UUID: " + signalPointUUIDs.getFirst());
        } else if (signalPointV.isEmpty()) {
            Transport.LOGGER.warn("Failed to find SignalPoint with UUID: " + signalPointUUIDs.getSecond());
        } else {
            this.network.addEdge(signalPointU.get(), signalPointV.get(), signalBlock);
        }
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        CompoundTag signaledEntitiesTag = new CompoundTag();
        this.signaledEntities.forEach(((uuid, signaledEntity) -> signaledEntitiesTag.put(uuid.toString(), signaledEntity.toTag())));
        pCompoundTag.put("SignaledEntities", signaledEntitiesTag);
        pCompoundTag.put("Network", NBTHelper.writeNetwork(network, SignalPoint::uuid, SignalPoint::toTag, SignalBlock::toTag));
        return pCompoundTag;
    }

    public boolean addSignalPoint(SignalPoint signalPoint) {
        boolean added = network.addNode(signalPoint);
        if (added) {
            this.setDirty();
        }
        return added;
    }

    public boolean removeSignalPoint(BlockPos signalPos) {
        return this.getSignalPointByBlockPos(signalPos)
                .map(this::removeSignalPoint)
                .orElse(false);
    }

    public boolean removeSignalPoint(SignalPoint signalPoint) {
        boolean removed = network.removeNode(signalPoint);
        if (removed) {
            this.setDirty();
        }
        return removed;
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

    public Optional<SignalBlock> getSignalBlockByUUID(UUID uuid) {
        return network.edges()
                .parallelStream()
                .filter(signalBlock -> signalBlock.getUuid().equals(uuid))
                .findFirst();
    }

    public Set<SignalBlock> getForwardSignalBlocks(SignalPoint signalPoint) {
        return network.outEdges(signalPoint);
    }

    public SignalPoint createSignalPoint(Block block, BlockPos blockPos) {
        SignalPoint signalPoint = new SignalPoint(UUID.randomUUID(), block, blockPos);
        if (!this.addSignalPoint(signalPoint)) {
            Transport.LOGGER.warn("Signal Point for {} already exists", blockPos.toString());
        } else {
            this.setDirty();
        }
        return signalPoint;
    }

    public void tick() {
        this.signaledEntities.values()
                .forEach(signaledEntity -> signaledEntity.update(this.serverLevel));
    }

    @ParametersAreNonnullByDefault
    public void onEntityPass(AbstractMinecart minecart, SignalPoint signalPoint) {
        if (this.signaledEntities.containsKey(minecart.getUUID())) {
            SignaledEntity signaledEntity = this.signaledEntities.get(minecart.getUUID());
            if (!signalPoint.equals(signaledEntity.getLastSignalPoint())) {
                //TODO make understand new point
                signaledEntity.setLastSignalPoint(signalPoint);
            }
        } else {
            SignaledEntity signaledEntity = new SignaledEntity(minecart.getUUID(), minecart.blockPosition());
            signaledEntity.setLastSignalPoint(signalPoint);
            this.signaledEntities.put(minecart.getUUID(), signaledEntity);
        }
        this.setDirty();
    }

    @ParametersAreNonnullByDefault
    public boolean createSignalBlock(SignalPoint signalPointU, SignalPoint signalPointV) {
        boolean changed = this.network.addEdge(signalPointU, signalPointV, new SignalBlock(UUID.randomUUID()));
        if (changed) {
            this.serverLevel.scheduleTick(signalPointU.blockPos(), signalPointU.block(), 1);
            this.setDirty();
        }
        return changed;
    }

    public SignaledEntity getSignaledEntity(AbstractMinecart minecart) {
        return this.signaledEntities.get(minecart.getUUID());
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
            this.setDirty();
            Transport.LOGGER.warn("Removed Signaled Entity");
        }
    }

    private void handleUnload(AbstractMinecart minecart) {
        if (this.signaledEntities.containsKey(minecart.getUUID())) {
            this.setDirty();
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

package xyz.brassgoggledcoders.transport.signal;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.util.LazyEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings({"UnstableApiUsage", "UnusedReturnValue"})
public class SignalLevelData extends SavedData {
    public static final String NAME = Transport.ID + ":signals";

    private final ServerLevel serverLevel;
    private final MutableNetwork<SignalPoint, SignalBlock> network;

    private final List<SignaledEntity> signaledEntities;

    public SignalLevelData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        this.network = NetworkBuilder.directed()
                .build();
        this.signaledEntities = Lists.newArrayList();
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

    public SignalPoint createSignalPoint(BlockPos blockPos) {
        SignalPoint signalPoint = new SignalPoint(UUID.randomUUID(), blockPos);
        if (!this.addSignalPoint(signalPoint)) {
            Transport.LOGGER.warn("Signal Point for {} already exists", blockPos.toString());
        }
        return signalPoint;
    }

    public void setMinecartOnSignalPoint(AbstractMinecart cart, BlockPos blockPos) {
        this.getSignalPointByBlockPos(blockPos)
                .ifPresent(signalPoint -> this.setMinecartOnSignalPoint(cart, signalPoint));
    }

    public void setMinecartOnSignalPoint(AbstractMinecart cart, SignalPoint signalPoint) {

    }

    public void tick() {

    }

    public void onChunkUnload(ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();

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

package xyz.brassgoggledcoders.transport.signal;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.Transport;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class SignalLevelData extends SavedData {
    private final ServerLevel serverLevel;
    private final MutableNetwork<SignalPoint, SignalBlock> network;

    public SignalLevelData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        this.network = NetworkBuilder.directed()
                .build();
    }

    public SignalLevelData(ServerLevel serverLevel, CompoundTag compoundTag) {
        this(serverLevel);
    }

    public boolean addSignalPoint(SignalPoint signalPoint) {
        return network.addNode(signalPoint);
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

    public Set<SignalBlock> getExitSignalBlocks(SignalPoint signalPoint) {
        return network.outEdges(signalPoint);
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        return new CompoundTag();
    }


    public static SignalLevelData getFor(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(
                        compoundTag -> new SignalLevelData(serverLevel, compoundTag),
                        () -> new SignalLevelData(serverLevel),
                        Transport.ID + ":signals"
                );
    }
}

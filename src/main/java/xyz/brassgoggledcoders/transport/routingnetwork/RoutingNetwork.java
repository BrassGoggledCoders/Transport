package xyz.brassgoggledcoders.transport.routingnetwork;

import com.google.common.collect.Maps;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class RoutingNetwork extends WorldSavedData {
    private final Map<UUID, RoutingNode> wayStations;
    private MutableValueGraph<RoutingNode, Double> internalNetwork;

    public RoutingNetwork(String name) {
        super(name);
        this.wayStations = Maps.newHashMap();
        this.internalNetwork = ValueGraphBuilder.undirected()
                .build();
    }

    public void add(RoutingNode routingNode) {
        this.wayStations.put(routingNode.getUniqueId(), routingNode);
        this.internalNetwork.addNode(routingNode);
        this.markDirty();
    }

    public void remove(RoutingNode routingNode) {
        this.wayStations.remove(routingNode.getUniqueId());
        this.internalNetwork.removeNode(routingNode);
        routingNode.setValid(false);
        this.markDirty();
    }

    @Nullable
    public RoutingNode get(@Nullable UUID uniqueId) {
        if (uniqueId == null) {
            return null;
        } else {
            return wayStations.get(uniqueId);
        }
    }

    public void join(RoutingNode routingNode, Collection<RoutingNode> neighborNodes) {
        for (RoutingNode neighborNode : neighborNodes) {
            double distance = routingNode.getPosition().distanceSq(neighborNode.getPosition());
            if (distance < 200) {
                internalNetwork.putEdgeValue(routingNode, neighborNode, distance);
            }
        }
        this.markDirty();
    }

    public List<RoutingNode> getNear(RoutingNode routingNode, int distance) {
        return wayStations.values()
                .parallelStream()
                .filter(value -> value != routingNode && value.getPosition().withinDistance(routingNode.getPosition(), distance))
                .collect(Collectors.toList());
    }

    public Optional<RoutingNode> getClosestStation(BlockPos blockPos) {
        return wayStations.values()
                .parallelStream()
                .filter(value -> value.getType() == RoutingNodeType.STATION)
                .min(Comparator.comparingInt(value -> value.getPosition().manhattanDistance(blockPos)));
    }

    public Collection<RoutingNode> getConnectedStations(RoutingNode routingNode) {
        return internalNetwork.adjacentNodes(routingNode);
    }

    public Collection<RoutingNode> getNeighbors(RoutingNode routingNode) {
        return internalNetwork.adjacentNodes(routingNode);
    }

    @Override
    public void read(@Nonnull CompoundNBT nbt) {
        ListNBT wayStationListNBT = nbt.getList("wayStations", Constants.NBT.TAG_COMPOUND);
        ListNBT connectionList = nbt.getList("edges", Constants.NBT.TAG_STRING);

        this.wayStations.clear();
        this.internalNetwork = ValueGraphBuilder.undirected()
                .build();
        for (int i = 0; i < wayStationListNBT.size(); i++) {
            CompoundNBT compoundNBT = wayStationListNBT.getCompound(i);
            this.add(RoutingNode.fromNBT(compoundNBT));
        }

        for (int i = 0; i < connectionList.size(); i++) {
            String[] nodeIds = connectionList.getString(i).split(" ");
            if (nodeIds.length == 2) {
                RoutingNode routingNodeU = this.get(UUID.fromString(nodeIds[0]));
                RoutingNode routingNodeV = this.get(UUID.fromString(nodeIds[1]));
                if (routingNodeU != null && routingNodeV != null) {
                    this.internalNetwork.putEdgeValue(
                            routingNodeU,
                            routingNodeV,
                            routingNodeU.getPosition().distanceSq(routingNodeV.getPosition())
                    );
                }
            }

        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        ListNBT wayStationListNBT = new ListNBT();
        for (RoutingNode routingNode : wayStations.values()) {
            wayStationListNBT.add(routingNode.toNBT());
        }
        compound.put("wayStations", wayStationListNBT);

        ListNBT edgeListNBT = new ListNBT();
        for (EndpointPair<RoutingNode> endpointPair : internalNetwork.edges()) {
            edgeListNBT.add(StringNBT.valueOf(endpointPair.nodeU().getUniqueId() + " " + endpointPair.nodeV().getUniqueId()));
        }
        compound.put("edges", edgeListNBT);

        return compound;
    }
}

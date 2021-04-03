package xyz.brassgoggledcoders.transport.routingnetwork;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class RoutingNetwork extends WorldSavedData {
    private final Map<UUID, RoutingNode> nodes;
    private MutableValueGraph<RoutingNode, RoutingEdge> internalNetwork;

    public RoutingNetwork(String name) {
        super(name);
        this.nodes = Maps.newHashMap();
        this.internalNetwork = ValueGraphBuilder.undirected()
                .build();
    }

    public void add(RoutingNode routingNode) {
        this.nodes.put(routingNode.getUniqueId(), routingNode);
        this.internalNetwork.addNode(routingNode);
        this.markDirty();
    }

    public void remove(RoutingNode routingNode) {
        this.nodes.remove(routingNode.getUniqueId());
        this.internalNetwork.removeNode(routingNode);
        routingNode.setValid(false);
        this.markDirty();
    }

    @Nullable
    public RoutingNode get(@Nullable UUID uniqueId) {
        if (uniqueId == null) {
            return null;
        } else {
            return nodes.get(uniqueId);
        }
    }

    public void join(RoutingNode routingNode, Collection<RoutingNode> neighborNodes) {
        for (RoutingNode neighborNode : neighborNodes) {
            double distance = routingNode.getPosition().distanceSq(neighborNode.getPosition());
            if (distance < 200) {
                internalNetwork.putEdgeValue(routingNode, neighborNode, new RoutingEdge(distance));
            }
        }
        this.markDirty();
    }

    public List<RoutingNode> getNear(RoutingNode routingNode, int distance) {
        return nodes.values()
                .parallelStream()
                .filter(value -> value != routingNode && value.getPosition().withinDistance(routingNode.getPosition(), distance))
                .collect(Collectors.toList());
    }

    public Optional<RoutingNode> getClosestStation(BlockPos blockPos) {
        return nodes.values()
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

    public List<Pair<RoutingNode, RoutingEdge>> getNeighborsWithEdges(RoutingNode routingNode) {
        List<Pair<RoutingNode, RoutingEdge>> neighbors = Lists.newArrayList();
        for (RoutingNode neighbor : this.getNeighbors(routingNode)) {
            neighbors.add(Pair.of(neighbor, this.internalNetwork.edgeValue(routingNode, neighbor)));
        }
        return neighbors;
    }

    @Override
    public void read(@Nonnull CompoundNBT nbt) {
        ListNBT wayStationListNBT = nbt.getList("nodes", Constants.NBT.TAG_COMPOUND);
        ListNBT connectionList = nbt.getList("edges", Constants.NBT.TAG_COMPOUND);

        this.nodes.clear();
        this.internalNetwork = ValueGraphBuilder.undirected()
                .build();
        for (int i = 0; i < wayStationListNBT.size(); i++) {
            CompoundNBT compoundNBT = wayStationListNBT.getCompound(i);
            this.add(RoutingNode.fromNBT(compoundNBT));
        }

        for (int i = 0; i < connectionList.size(); i++) {
            CompoundNBT connectionNBT = connectionList.getCompound(i);
            RoutingNode routingNodeU = this.get(connectionNBT.getUniqueId("u"));
            RoutingNode routingNodeV = this.get(connectionNBT.getUniqueId("v"));
            RoutingEdge routingEdge = RoutingEdge.fromNBT(connectionNBT);

            if (routingNodeU != null && routingNodeV != null) {
                this.internalNetwork.putEdgeValue(routingNodeU, routingNodeV, routingEdge);
            }
        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        ListNBT nodeListNBT = new ListNBT();
        for (RoutingNode routingNode : nodes.values()) {
            nodeListNBT.add(routingNode.toNBT());
        }
        compound.put("nodes", nodeListNBT);

        ListNBT edgeListNBT = new ListNBT();
        for (EndpointPair<RoutingNode> endpointPair : internalNetwork.edges()) {
            RoutingEdge routingEdge = internalNetwork.edgeValue(endpointPair.nodeU(), endpointPair.nodeV());
            CompoundNBT connectionNBT = routingEdge.toNBT();
            connectionNBT.putUniqueId("u", endpointPair.nodeU().getUniqueId());
            connectionNBT.putUniqueId("v", endpointPair.nodeV().getUniqueId());
        }
        compound.put("edges", edgeListNBT);

        return compound;
    }
}

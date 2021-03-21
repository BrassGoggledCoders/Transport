package xyz.brassgoggledcoders.transport.routingnetwork;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import gigaherz.graph2.Graph;
import gigaherz.graph2.GraphObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.content.TransportLoots;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class RoutingNetwork extends WorldSavedData {
    private final Map<UUID, RoutingNode> wayStations;

    public RoutingNetwork(String name) {
        super(name);
        this.wayStations = Maps.newHashMap();
    }

    public void add(RoutingNode routingNode) {
        this.wayStations.put(routingNode.getUniqueId(), routingNode);
        this.markDirty();
    }

    public void remove(RoutingNode routingNode) {
        this.wayStations.remove(routingNode.getUniqueId());
        routingNode.setValid(false);
        if (routingNode.getGraph() != null) {
            routingNode.getGraph().remove(routingNode);
        }
        this.markDirty();
    }

    public RoutingNode get(UUID uniqueId) {
        return wayStations.get(uniqueId);
    }

    public void join(RoutingNode routingNodeOne, List<RoutingNode> neighborNodes) {
        List<GraphObject> neighborObjects = Lists.newArrayList(neighborNodes);
        Graph.integrate(routingNodeOne, neighborObjects);
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

    public List<RoutingNode> getConnectedStations(RoutingNode routingNode) {
        if (routingNode.getGraph() != null) {
            return routingNode.getGraph()
                    .getObjects()
                    .parallelStream()
                    .filter(value -> value instanceof RoutingNode)
                    .map(value -> (RoutingNode) value)
                    .filter(value -> value.getType() == RoutingNodeType.STATION)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void read(@Nonnull CompoundNBT nbt) {
        ListNBT wayStationListNBT = nbt.getList("wayStations", Constants.NBT.TAG_COMPOUND);
        ListNBT connectionList = nbt.getList("connectedUUIDs", Constants.NBT.TAG_LIST);

        this.wayStations.clear();

        for (int i = 0; i < wayStationListNBT.size(); i++) {
            CompoundNBT compoundNBT = wayStationListNBT.getCompound(i);
            this.add(RoutingNode.fromNBT(compoundNBT));
        }

        for (int connectionListPos = 0; connectionListPos < connectionList.size(); connectionListPos++) {
            List<GraphObject> routingNodes = Lists.newArrayList();
            ListNBT routingNodeListNBT = connectionList.getList(connectionListPos);
            for (int routingNodeListPos = 0; routingNodeListPos < routingNodeListNBT.size(); routingNodeListPos++) {
                RoutingNode routingNode = this.wayStations.get(UUID.fromString(routingNodeListNBT.getString(routingNodeListPos)));
                if (routingNode != null) {
                    routingNodes.add(routingNode);
                }
            }
            if (!routingNodes.isEmpty()) {
                GraphObject primaryNode = routingNodes.get(0);
                Graph.integrate(primaryNode, Collections.emptyList());
                if (routingNodes.size() > 1) {
                    Iterator<GraphObject> routingNodeIterator = routingNodes.listIterator(1);
                    while (routingNodeIterator.hasNext()) {
                        Graph.connect(primaryNode, routingNodeIterator.next());
                    }
                }
            }
        }
        for (RoutingNode routingNode : this.wayStations.values()) {
            if (routingNode.getGraph() == null) {
                Graph.integrate(routingNode, Collections.emptyList());
                Transport.LOGGER.warn("Failed to Handle: " + routingNode.getGraph());
            }
        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        Set<Graph> graphs = Sets.newHashSet();

        ListNBT wayStationListNBT = new ListNBT();
        ListNBT connectionListNBT = new ListNBT();

        for (RoutingNode routingNode : wayStations.values()) {
            wayStationListNBT.add(routingNode.toNBT());
            Graph graph = routingNode.getGraph();
            if (graph != null) {
                graphs.add(graph);
            }
        }

        for (Graph graph : graphs) {
            Collection<GraphObject> graphObjects = graph.getObjects();
            if (!graphObjects.isEmpty()) {
                ListNBT connectedUUIDs = new ListNBT();
                for (GraphObject graphObject : graphObjects) {
                    if (graphObject instanceof RoutingNode) {
                        connectedUUIDs.add(StringNBT.valueOf(((RoutingNode) graphObject).getUniqueId().toString()));
                    }
                }
                connectionListNBT.add(connectedUUIDs);
            }

        }

        compound.put("wayStations", wayStationListNBT);
        compound.put("connectedUUIDs", connectionListNBT);

        return compound;
    }
}

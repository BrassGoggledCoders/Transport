package xyz.brassgoggledcoders.transport.routingnetwork;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RoutingDevice implements INBTSerializable<CompoundNBT> {
    private final RoutingNetworks type;
    private final IWorld world;
    private final Supplier<BlockPos> blockPosSupplier;
    private final Consumer<RoutingNode> onUpdatedNextNode;

    private final List<UUID> currentRoute;
    private final List<List<UUID>> potentialRoutes;

    private RoutingNode previousNode;
    private UUID previousNodeUniqueId;

    private RoutingNode nextNode;
    private UUID nextNodeUniqueId;

    private RoutingNode destinationNode;
    private UUID destinationNodeUniqueId;

    public RoutingDevice(RoutingNetworks type, IWorld world, Supplier<BlockPos> blockPosSupplier,
                         Consumer<RoutingNode> onUpdatedNextNode) {
        this.type = type;
        this.world = world;
        this.blockPosSupplier = blockPosSupplier;
        this.onUpdatedNextNode = onUpdatedNextNode;
        this.currentRoute = Lists.newArrayList();
        this.potentialRoutes = Lists.newArrayList();
    }

    public RoutingNode getPreviousNode() {
        if (this.previousNode == null && this.previousNodeUniqueId != null) {
            RoutingNetwork routingNetwork = type.getFor(this.world);
            if (routingNetwork != null) {
                this.previousNode = routingNetwork.get(this.previousNodeUniqueId);
            }
            this.previousNodeUniqueId = null;
        }
        return this.previousNode;
    }

    public void setPreviousNode(RoutingNode previousNode) {
        this.previousNode = previousNode;
        this.previousNodeUniqueId = (previousNode != null) ? previousNode.getUniqueId() : null;
    }

    public RoutingNode getNextNode() {
        if (this.nextNode == null && this.nextNodeUniqueId != null) {
            RoutingNetwork routingNetwork = type.getFor(this.world);
            if (routingNetwork != null) {
                this.nextNode = routingNetwork.get(this.nextNodeUniqueId);
            }
            this.nextNodeUniqueId = null;
        }
        return this.nextNode;
    }

    public void setNextNode(RoutingNode nextNode) {
        this.nextNode = nextNode;
        this.nextNodeUniqueId = (nextNode != null) ? nextNode.getUniqueId() : null;
        this.onUpdatedNextNode.accept(nextNode);
    }

    public RoutingNode getDestinationNode() {
        if (this.destinationNode == null && this.destinationNodeUniqueId != null) {
            RoutingNetwork routingNetwork = type.getFor(this.world);
            if (routingNetwork != null) {
                this.destinationNode = routingNetwork.get(this.destinationNodeUniqueId);
            }
            this.destinationNodeUniqueId = null;
        }
        return this.destinationNode;
    }

    public void setDestinationNode(RoutingNode destinationNode) {
        if (destinationNode == null || this.getPreviousNode() != destinationNode) {
            this.destinationNode = destinationNode;
            this.destinationNodeUniqueId = (destinationNode != null) ? destinationNode.getUniqueId() : null;
            this.currentRoute.clear();
            this.potentialRoutes.clear();
        }
    }

    public void reachedNextNode() {
        if (this.getNextNode() != null) {
            if (this.getNextNode() == this.getDestinationNode()) {
                this.setDestinationNode(null);
            }
            this.setPreviousNode(this.getNextNode());
        }
        this.setNextNode(null);
    }

    public void tick() {
        RoutingNetwork routingNetwork = this.type.getFor(this.world);
        if (routingNetwork != null) {
            if (this.getDestinationNode() != null) {
                if (this.getDestinationNode() == this.getPreviousNode()) {
                    this.setDestinationNode(null);
                }
                if (this.getNextNode() != null) {
                    if (!routingNetwork.areRoutingNodesConnected(this.getDestinationNode(), this.getNextNode())) {
                        this.setDestinationNode(null);
                    } else {
                        BlockPos currentPosition = this.blockPosSupplier.get();
                        if (currentPosition.manhattanDistance(this.getNextNode().getPosition()) < 8) {
                            this.reachedNextNode();
                        }
                    }
                } else {
                    if (this.currentRoute.isEmpty()) {
                        if (this.potentialRoutes.isEmpty()) {
                            if (this.getPreviousNode() != null) {
                                List<RoutingNode> neighbors = routingNetwork.getNeighbors(this.getPreviousNode());
                                for (RoutingNode neighbor : neighbors) {
                                    this.potentialRoutes.add(Lists.newArrayList(
                                            this.getPreviousNode().getUniqueId(), neighbor.getUniqueId()
                                    ));
                                }
                            } else {
                                routingNetwork.getClosestStation(blockPosSupplier.get())
                                        .ifPresent(this::setPreviousNode);
                            }
                        } else {
                            Iterator<List<UUID>> routesToCheck = this.potentialRoutes.iterator();
                            List<List<UUID>> newlyFoundRoutes = Lists.newArrayList();
                            int numberChecked = 0;
                            while (routesToCheck.hasNext() && numberChecked < 5 && this.currentRoute.isEmpty()) {
                                List<UUID> routeToCheck = routesToCheck.next();
                                RoutingNode lastInRoute = routingNetwork.get(Iterables.getLast(routeToCheck, null));
                                if (lastInRoute != null) {
                                    List<RoutingNode> neighbors = routingNetwork.getNeighbors(lastInRoute);
                                    for (RoutingNode neighbor : neighbors) {
                                        if (neighbor == this.getDestinationNode()) {
                                            this.currentRoute.addAll(routeToCheck);
                                            this.currentRoute.add(neighbor.getUniqueId());
                                        }
                                        if (!routeToCheck.contains(neighbor.getUniqueId())) {
                                            List<UUID> copiedRoute = Lists.newArrayList(routeToCheck);
                                            copiedRoute.add(neighbor.getUniqueId());
                                            newlyFoundRoutes.add(copiedRoute);
                                        }
                                    }
                                }
                                routesToCheck.remove();

                                numberChecked++;
                            }
                            this.potentialRoutes.addAll(newlyFoundRoutes);
                            if (!this.currentRoute.isEmpty()) {
                                this.potentialRoutes.clear();
                            }
                        }
                    } else {
                        RoutingNode routingNode = routingNetwork.get(this.currentRoute.remove(0));
                        if (routingNode != null) {
                            this.setNextNode(routingNode);
                        }
                    }
                }
            } else {
                this.currentRoute.clear();
                this.potentialRoutes.clear();
            }
        }

    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        if (this.nextNode != null) {
            compoundNBT.putUniqueId("nextNode", this.nextNode.getUniqueId());
        } else if (this.nextNodeUniqueId != null) {
            compoundNBT.putUniqueId("nextNode", this.nextNodeUniqueId);
        }
        if (this.destinationNode != null) {
            compoundNBT.putUniqueId("destinationNode", this.destinationNode.getUniqueId());
        } else if (this.destinationNodeUniqueId != null) {
            compoundNBT.putUniqueId("destinationNode", this.destinationNodeUniqueId);
        }
        if (this.previousNode != null) {
            compoundNBT.putUniqueId("previousNode", this.previousNode.getUniqueId());
        } else if (this.previousNodeUniqueId != null) {
            compoundNBT.putUniqueId("previousNode", this.previousNodeUniqueId);
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.hasUniqueId("destinationNode")) {
            this.destinationNodeUniqueId = nbt.getUniqueId("destinationNode");
        }
        if (nbt.hasUniqueId("nextNode")) {
            this.nextNodeUniqueId = nbt.getUniqueId("nextNode");
        }
        if (nbt.hasUniqueId("previousNode")) {
            this.previousNodeUniqueId = nbt.getUniqueId("previousNode");
        }
    }
}

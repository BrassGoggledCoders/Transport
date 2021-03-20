package xyz.brassgoggledcoders.transport.routingnetwork;

import gigaherz.graph2.Graph;
import gigaherz.graph2.GraphObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class RoutingNode implements GraphObject {
    private final BlockPos position;
    private final UUID uniqueId;
    private final RoutingNodeType type;

    private boolean valid = true;
    private Graph graph;

    public RoutingNode(BlockPos position, RoutingNodeType type) {
        this.position = position;
        this.type = type;
        this.uniqueId = UUID.randomUUID();
    }

    public RoutingNode(BlockPos position, UUID uniqueId, RoutingNodeType type) {
        this.position = position;
        this.uniqueId = uniqueId;
        this.type = type;
    }

    public BlockPos getPosition() {
        return position;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    @Override
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public RoutingNodeType getType() {
        return type;
    }

    public CompoundNBT toNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putUniqueId("uniqueId", this.uniqueId);
        compoundNBT.put("location", NBTUtil.writeBlockPos(this.position));
        compoundNBT.putString("type", this.type.name());
        return compoundNBT;
    }

    public static RoutingNode fromNBT(CompoundNBT compoundNBT) {
        return new RoutingNode(
                NBTUtil.readBlockPos(compoundNBT.getCompound("location")),
                compoundNBT.getUniqueId("uniqueId"),
                RoutingNodeType.valueOf(compoundNBT.getString("type"))
        );
    }
}

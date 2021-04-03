package xyz.brassgoggledcoders.transport.routingnetwork;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class RoutingEdge {
    private final double distance;
    private boolean active;

    public RoutingEdge(double distance) {
        this.distance = distance;
        this.active = true;
    }

    public RoutingEdge(double distance, boolean active) {
        this.distance = distance;
        this.active = active;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CompoundNBT toNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putDouble("d", distance);
        compoundNBT.putBoolean("a", active);
        return compoundNBT;
    }

    public static RoutingEdge fromNBT(CompoundNBT connectionNBT) {
        return new RoutingEdge(connectionNBT.getDouble("d"), connectionNBT.getBoolean("a"));
    }

    public void toBuffer(PacketBuffer packetBuffer) {
        packetBuffer.writeDouble(this.distance);
        packetBuffer.writeBoolean(this.active);
    }

    public static RoutingEdge fromBuffer(PacketBuffer packetBuffer) {
        return new RoutingEdge(
                packetBuffer.readDouble(),
                packetBuffer.readBoolean()
        );
    }
}

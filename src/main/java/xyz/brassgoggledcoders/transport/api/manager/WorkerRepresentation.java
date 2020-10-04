package xyz.brassgoggledcoders.transport.api.manager;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class WorkerRepresentation {
    private final BlockPos blockPos;
    private final ItemStack representative;
    private final UUID uniqueId;


    public WorkerRepresentation(BlockPos blockPos, ItemStack representative, UUID uniqueId) {
        this.blockPos = blockPos;
        this.representative = representative;
        this.uniqueId = uniqueId;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public ItemStack getRepresentative() {
        return representative;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public CompoundNBT toCompoundNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("blockPos", blockPos.toLong());
        nbt.put("representative", this.representative.write(new CompoundNBT()));
        nbt.putUniqueId("uniqueId", this.uniqueId);
        return nbt;
    }

    public void toPackerBuffer(PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(this.blockPos);
        packetBuffer.writeItemStack(this.getRepresentative());
        packetBuffer.writeUniqueId(this.uniqueId);
    }

    public static WorkerRepresentation fromCompoundNBT(CompoundNBT nbt) {
        return new WorkerRepresentation(
                BlockPos.fromLong(nbt.getLong("blockPos")),
                ItemStack.read(nbt.getCompound("representative")),
                nbt.getUniqueId("uniqueId")
        );
    }

    public static WorkerRepresentation fromPacketBuffer(PacketBuffer packetBuffer) {
        return new WorkerRepresentation(
                packetBuffer.readBlockPos(),
                packetBuffer.readItemStack(),
                packetBuffer.readUniqueId()
        );
    }

    public static WorkerRepresentation fromWorker(IWorker worker) {
        return new WorkerRepresentation(
                worker.getWorkerPos(),
                worker.getRepresentative(),
                worker.getUniqueId()
        );
    }
}

package xyz.brassgoggledcoders.transport.api.manager;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class ManagedObject {
    private final BlockPos blockPos;
    private final ItemStack representative;
    private final UUID uniqueId;

    public ManagedObject(BlockPos blockPos, ItemStack representative, UUID uniqueId) {
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

    public static ManagedObject fromCompoundNBT(CompoundNBT nbt) {
        return new ManagedObject(
                BlockPos.fromLong(nbt.getLong("blockPos")),
                ItemStack.read(nbt.getCompound("representative")),
                nbt.getUniqueId("uniqueId")
        );
    }

    public static ManagedObject fromPacketBuffer(PacketBuffer packetBuffer) {
        return new ManagedObject(
                packetBuffer.readBlockPos(),
                packetBuffer.readItemStack(),
                packetBuffer.readUniqueId()
        );
    }

    public static ManagedObject fromManageable(IManageable manageable, BlockPos blockPos, BlockState blockState) {
        return new ManagedObject(
                blockPos,
                manageable.hasCustomRepresentative() ? manageable.getCustomRepresentative() :
                        new ItemStack(blockState.getBlock()),
                manageable.getUniqueId()
        );
    }
}

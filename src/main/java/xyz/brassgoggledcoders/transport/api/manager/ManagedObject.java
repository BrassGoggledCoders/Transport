package xyz.brassgoggledcoders.transport.api.manager;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;
import java.util.function.Predicate;

public class ManagedObject {
    private final BlockPos blockPos;
    private final ItemStack representative;
    private final UUID uniqueId;

    private ItemStack importPredicateStack;
    private ItemStack exportPredicateStack;

    private Predicate<Entity> importPredicate = entity -> true;
    private Predicate<Entity> exportPredicate = entity -> true;

    public ManagedObject(BlockPos blockPos, ItemStack representative, UUID uniqueId, ItemStack importPredicateStack,
                         ItemStack exportPredicateStack) {
        this.blockPos = blockPos;
        this.representative = representative;
        this.uniqueId = uniqueId;
        this.importPredicateStack = importPredicateStack;
        this.exportPredicateStack = exportPredicateStack;
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

    public Predicate<Entity> getExportPredicate() {
        return exportPredicate;
    }

    public Predicate<Entity> getImportPredicate() {
        return importPredicate;
    }

    public boolean attemptSetExportPredicateStack(ItemStack itemStack) {
        this.exportPredicateStack = itemStack;
        return true;
    }

    public boolean attemptSetImportPredicateStack(ItemStack itemStack) {
        this.importPredicateStack = itemStack;
        return true;
    }

    public CompoundNBT toCompoundNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("blockPos", blockPos.toLong());
        nbt.put("representative", this.representative.write(new CompoundNBT()));
        nbt.putUniqueId("uniqueId", this.uniqueId);
        nbt.put("importPredicateStack", this.importPredicateStack.write(new CompoundNBT()));
        nbt.put("exportPredicateStack", this.exportPredicateStack.write(new CompoundNBT()));
        return nbt;
    }

    public void toPackerBuffer(PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(this.blockPos);
        packetBuffer.writeItemStack(this.getRepresentative());
        packetBuffer.writeUniqueId(this.uniqueId);
        packetBuffer.writeItemStack(this.importPredicateStack);
        packetBuffer.writeItemStack(this.exportPredicateStack);
    }

    public static ManagedObject fromCompoundNBT(CompoundNBT nbt) {
        return new ManagedObject(
                BlockPos.fromLong(nbt.getLong("blockPos")),
                ItemStack.read(nbt.getCompound("representative")),
                nbt.getUniqueId("uniqueId"),
                ItemStack.read(nbt.getCompound("importPredicateStack")),
                ItemStack.read(nbt.getCompound("exportPredicateStack"))
        );
    }

    public static ManagedObject fromPacketBuffer(PacketBuffer packetBuffer) {
        return new ManagedObject(
                packetBuffer.readBlockPos(),
                packetBuffer.readItemStack(),
                packetBuffer.readUniqueId(),
                packetBuffer.readItemStack(),
                packetBuffer.readItemStack()
        );
    }

    public static ManagedObject fromManageable(IWorker manageable, BlockPos blockPos, BlockState blockState) {
        return new ManagedObject(
                blockPos,
                manageable.hasCustomRepresentative() ? manageable.getCustomRepresentative() :
                        new ItemStack(blockState.getBlock()),
                manageable.getUniqueId(),
                ItemStack.EMPTY,
                ItemStack.EMPTY
        );
    }
}

package xyz.brassgoggledcoders.transport.api.master;

import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class Manager implements IManager {
    private final NonNullLazy<BlockPos> position;
    private final NonNullLazy<AxisAlignedBB> boundary;
    private final ManagerType type;
    private final Set<ManagedObject> managedObjects;

    private UUID uniqueId;

    public Manager(NonNullSupplier<BlockPos> positionSupplier, NonNullSupplier<AxisAlignedBB> boundarySupplier,
                   ManagerType type) {
        this.position = NonNullLazy.of(positionSupplier);
        this.boundary = NonNullLazy.of(boundarySupplier);
        this.type = type;
        this.managedObjects = Sets.newHashSet();
        this.uniqueId = UUID.randomUUID();
    }

    @Override
    @Nonnull
    public BlockPos getPosition() {
        return position.get();
    }

    @Override
    @Nonnull
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    @Nonnull
    public ManagerType getType() {
        return type;
    }

    @Override
    public boolean addManagedObject(@Nonnull ManagedObject managedObject) {
        BlockPos blockPos = managedObject.getBlockPos();
        if (!blockPos.equals(this.getPosition()) && this.getBoundary().contains(blockPos.getX(), blockPos.getY(),
                blockPos.getZ())) {
            return this.getManagedObjects().add(managedObject);
        }

        return false;
    }

    @Override
    @Nonnull
    public Collection<ManagedObject> getManagedObjects() {
        return managedObjects;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundary() {
        return boundary.get();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("uniqueId", this.uniqueId);
        ListNBT managedObjectsList = new ListNBT();
        for (ManagedObject managedObject : this.managedObjects) {
            managedObjectsList.add(managedObject.toCompoundNBT());
        }
        nbt.put("managedObjects", managedObjectsList);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.uniqueId = nbt.getUniqueId("uniqueId");
        this.managedObjects.clear();
        ListNBT connectedObjectNBT = nbt.getList("managedObjects", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < connectedObjectNBT.size(); i++) {
            this.managedObjects.add(new ManagedObject(connectedObjectNBT.getCompound(i)));
        }
    }

    public void writeToPacketBuffer(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(this.managedObjects.size());
        for (ManagedObject managedObject : this.managedObjects) {
            packetBuffer.writeLong(managedObject.getBlockPos().toLong());
            packetBuffer.writeItemStack(managedObject.getRepresentative());
        }
    }
}

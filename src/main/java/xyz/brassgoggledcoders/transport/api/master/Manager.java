package xyz.brassgoggledcoders.transport.api.master;

import com.google.common.collect.Maps;
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
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class Manager implements IManager {
    private final Consumer<?> markDirty;
    private final NonNullLazy<BlockPos> position;
    private final NonNullLazy<AxisAlignedBB> boundary;
    private final ManagerType type;
    private final Map<UUID, ManagedObject> managedObjects;

    private UUID uniqueId;


    public Manager(NonNullSupplier<BlockPos> positionSupplier, NonNullSupplier<AxisAlignedBB> boundarySupplier,
                   ManagerType type, Consumer<?> markDirty) {
        this.position = NonNullLazy.of(positionSupplier);
        this.boundary = NonNullLazy.of(boundarySupplier);
        this.type = type;
        this.markDirty = markDirty;
        this.managedObjects = Maps.newLinkedHashMap();
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
        if (!managedObjects.containsKey(managedObject.getUniqueId()) && !blockPos.equals(this.getPosition()) &&
                this.getBoundary().contains(blockPos.getX(), blockPos.getY(), blockPos.getZ())) {
            this.managedObjects.put(managedObject.getUniqueId(), managedObject);
            this.markDirty.accept(null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public Collection<ManagedObject> getManagedObjects() {
        return managedObjects.values();
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
        for (ManagedObject managedObject : this.managedObjects.values()) {
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
            ManagedObject managedObject = ManagedObject.fromCompoundNBT(connectedObjectNBT.getCompound(i));
            this.managedObjects.put(managedObject.getUniqueId(), managedObject);
        }
    }

    public void writeToPacketBuffer(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(this.managedObjects.size());
        for (ManagedObject managedObject : this.managedObjects.values()) {
            managedObject.toPackerBuffer(packetBuffer);
        }
    }
}

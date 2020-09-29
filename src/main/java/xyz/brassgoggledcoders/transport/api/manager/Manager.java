package xyz.brassgoggledcoders.transport.api.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.*;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.transfer.ITransferor;

import javax.annotation.Nonnull;
import java.util.*;
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
    public boolean handleUnloading(@Nonnull Entity leader, @Nonnull List<Entity> followers) {
        return transfer(leader, followers, ITransferor::transfer);
    }

    @Override
    public boolean handleLoading(@Nonnull Entity leader, @Nonnull List<Entity> followers) {
        return transfer(leader, followers, (transferor, entity, managed) -> transferor.transfer(managed, entity));
    }

    private boolean transfer(Entity leader, List<Entity> followers, TriPredicate<ITransferor<?>, ICapabilityProvider,
            ICapabilityProvider> transfer) {
        boolean didSomething = false;
        List<Pair<ManagedObject, TileEntity>> matchedObjects = Lists.newArrayList();
        for (ManagedObject managedObject : this.getManagedObjects()) {
            if (managedObject.getImportPredicate().test(leader)) {
                TileEntity tileEntity = leader.getEntityWorld().getTileEntity(managedObject.getBlockPos());
                if (tileEntity != null) {
                    if (tileEntity.getCapability(TransportAPI.MANAGEABLE).map(IManageable::getUniqueId)
                            .map(managedObject.getUniqueId()::equals)
                            .orElse(false)) {
                        matchedObjects.add(Pair.of(managedObject, tileEntity));
                    }
                }
            }
        }
        for (ITransferor<?> transferor : TransportAPI.getTransferors()) {
            LazyOptional<?> leaderLazy = leader.getCapability(transferor.getCapability());
            List<LazyOptional<?>> allLazies = new ArrayList<>();
            if (leaderLazy.isPresent()) {
                allLazies.add(leaderLazy);
            }
            for (Entity entity : followers) {
                LazyOptional<?> lazyOptional = entity.getCapability(transferor.getCapability());
                if (lazyOptional.isPresent()) {
                    allLazies.add(lazyOptional);
                }
            }
            if (!allLazies.isEmpty()) {
                SingleCapabilityProvider managedProvider = new SingleCapabilityProvider();
                SingleCapabilityProvider entityProvider = new SingleCapabilityProvider();
                for (Pair<ManagedObject, TileEntity> managedObject : matchedObjects) {
                    LazyOptional<?> managedLazy = managedObject.getRight().getCapability(transferor.getCapability());
                    for (LazyOptional<?> entityLazy : allLazies) {
                        didSomething |= transfer.test(transferor, entityProvider.withCurrentCap(entityLazy),
                                managedProvider.withCurrentCap(managedLazy));
                    }
                }
            }
        }
        return !didSomething;
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

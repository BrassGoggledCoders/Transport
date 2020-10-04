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
import xyz.brassgoggledcoders.transport.api.TransportCapabilities;
import xyz.brassgoggledcoders.transport.api.transfer.ITransferor;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Manager implements IManager {
    private final Consumer<?> markDirty;
    private final NonNullLazy<BlockPos> position;
    private final NonNullLazy<AxisAlignedBB> boundary;
    private final ManagerType type;
    private final Map<UUID, WorkerRepresentation> workerRepresentations;

    private UUID uniqueId;


    public Manager(NonNullSupplier<BlockPos> positionSupplier, NonNullSupplier<AxisAlignedBB> boundarySupplier,
                   ManagerType type, Consumer<?> markDirty) {
        this.position = NonNullLazy.of(positionSupplier);
        this.boundary = NonNullLazy.of(boundarySupplier);
        this.type = type;
        this.markDirty = markDirty;
        this.workerRepresentations = Maps.newLinkedHashMap();
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
    public boolean addWorker(@Nonnull IWorker worker) {
        BlockPos blockPos = worker.getWorkerPos();
        if (worker.isValidManager(this) && !workerRepresentations.containsKey(worker.getUniqueId()) &&
                this.getBoundary().contains(blockPos.getX(), blockPos.getY(), blockPos.getZ())) {
            worker.setManagerPos(this.getPosition());
            this.workerRepresentations.put(worker.getUniqueId(), WorkerRepresentation.fromWorker(worker));
            this.markDirty.accept(null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeWorker(@Nonnull IWorker worker) {
        return this.workerRepresentations.remove(worker.getUniqueId()) != null;
    }

    @Override
    @Nonnull
    public Collection<WorkerRepresentation> getWorkerRepresentatives() {
        return workerRepresentations.values();
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundary() {
        return boundary.get();
    }

    @Override
    public boolean handleUnloading(@Nonnull Entity leader, @Nonnull List<Entity> followers) {
        return transfer(leader, followers, workerRepresentation -> entity -> true, ITransferor::transfer);
    }

    @Override
    public boolean handleLoading(@Nonnull Entity leader, @Nonnull List<Entity> followers) {
        return transfer(leader, followers, workerRepresentation -> entity -> true,
                (transferor, entity, managed) -> transferor.transfer(managed, entity));
    }

    private boolean transfer(Entity leader, List<Entity> followers, Function<WorkerRepresentation, Predicate<Entity>> predicateFunction,
                             TriPredicate<ITransferor<?>, ICapabilityProvider, ICapabilityProvider> transfer) {
        boolean didSomething = false;
        List<Pair<WorkerRepresentation, TileEntity>> matchedObjects = Lists.newArrayList();
        for (WorkerRepresentation workerRepresentation : this.getWorkerRepresentatives()) {
            if (predicateFunction.apply(workerRepresentation).test(leader)) {
                TileEntity tileEntity = leader.getEntityWorld().getTileEntity(workerRepresentation.getBlockPos());
                if (tileEntity != null) {
                    if (tileEntity.getCapability(TransportCapabilities.WORKER).map(IWorker::getUniqueId)
                            .map(workerRepresentation.getUniqueId()::equals)
                            .orElse(false)) {
                        matchedObjects.add(Pair.of(workerRepresentation, tileEntity));
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
                for (Pair<WorkerRepresentation, TileEntity> managedObject : matchedObjects) {
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
        ListNBT workerRepresentativeListNBT = new ListNBT();
        for (WorkerRepresentation workerRepresentation : this.workerRepresentations.values()) {
            workerRepresentativeListNBT.add(workerRepresentation.toCompoundNBT());
        }
        nbt.put("workerRepresentatives", workerRepresentativeListNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.uniqueId = nbt.getUniqueId("uniqueId");
        this.workerRepresentations.clear();
        ListNBT workerRepresentativeListNBT = nbt.getList("workerRepresentatives", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < workerRepresentativeListNBT.size(); i++) {
            WorkerRepresentation workerRepresentation = WorkerRepresentation.fromCompoundNBT(
                    workerRepresentativeListNBT.getCompound(i));
            this.workerRepresentations.put(workerRepresentation.getUniqueId(), workerRepresentation);
        }
    }

    public void writeToPacketBuffer(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(this.workerRepresentations.size());
        for (WorkerRepresentation workerRepresentation : this.workerRepresentations.values()) {
            workerRepresentation.toPackerBuffer(packetBuffer);
        }
    }
}

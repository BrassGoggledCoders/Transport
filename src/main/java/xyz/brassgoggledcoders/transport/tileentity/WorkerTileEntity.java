package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.TransportCapabilities;
import xyz.brassgoggledcoders.transport.api.event.GetWorkerRepresentativeEvent;
import xyz.brassgoggledcoders.transport.api.manager.IWorker;
import xyz.brassgoggledcoders.transport.api.manager.Worker;
import xyz.brassgoggledcoders.transport.block.WorkerBlock;
import xyz.brassgoggledcoders.transport.capability.supervisor.TileEntityCapabilitySupervisor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class WorkerTileEntity extends TileEntity {
    private final Worker worker;
    private final LazyOptional<IWorker> workerLazy;

    private TileEntityCapabilitySupervisor capabilitySupervisor;

    public WorkerTileEntity(TileEntityType<? extends WorkerTileEntity> tileEntityType) {
        super(tileEntityType);
        this.worker = new Worker(null, this::getWorkerRepresentative, this::getPos);
        this.workerLazy = LazyOptional.of(() -> worker);
    }

    @Nonnull
    private ItemStack getWorkerRepresentative() {
        TileEntity workedTileEntity = this.getWorkedTileEntity();
        if (workedTileEntity != null) {
            GetWorkerRepresentativeEvent<TileEntity> event = new GetWorkerRepresentativeEvent<>(workedTileEntity, worker);
            MinecraftForge.EVENT_BUS.post(event);
            ItemStack itemStack = event.getRepresentative();
            if (itemStack.isEmpty()) {
                itemStack = new ItemStack(workedTileEntity.getBlockState().getBlock());
            }
            if (itemStack.isEmpty()) {
                itemStack = new ItemStack(this.getBlockState().getBlock());
                itemStack.setDisplayName(new TranslationTextComponent(workedTileEntity.getBlockState()
                        .getBlock()
                        .getTranslationKey()
                ));
            }
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    public void checkValidity() {
        if (capabilitySupervisor != null && !capabilitySupervisor.isValid()) {
            capabilitySupervisor = null;
        }
        if (capabilitySupervisor == null) {
            TileEntity tileEntity = this.getWorkedTileEntity();
            if (tileEntity != null) {
                this.capabilitySupervisor = new TileEntityCapabilitySupervisor(tileEntity);
            }
        }
    }

    @Nullable
    private TileEntity getWorkedTileEntity() {
        if (this.getWorld() != null) {
            return this.getWorld().getTileEntity(this.getPos().offset(this.getBlockState().get(WorkerBlock.FACING).getOpposite()));
        } else {
            return null;
        }
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.workerLazy.invalidate();
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt = super.write(nbt);
        nbt.put("worker", worker.serializeNBT());
        return nbt;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState blockState, CompoundNBT nbt) {
        super.read(blockState, nbt);
        this.worker.deserializeNBT(nbt.getCompound("worker"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == TransportCapabilities.WORKER) {
            return workerLazy.cast();
        } else if (TransportAPI.getTransferor(cap) != null) {
            checkValidity();
            if (capabilitySupervisor != null) {
                return capabilitySupervisor.getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }
}

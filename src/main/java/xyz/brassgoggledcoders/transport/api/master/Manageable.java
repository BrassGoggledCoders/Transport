package xyz.brassgoggledcoders.transport.api.master;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.common.util.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.TransportAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Manageable implements IManageable {
    private final ManagerType type;
    private LazyOptional<IManager> manager;
    private BlockPos managerPos;
    private final NonNullLazy<BlockPos> pos;
    private final NonNullSupplier<ItemStack> itemStackSupplier;

    public Manageable(NonNullSupplier<BlockPos> blockPosSupplier, NonNullSupplier<ItemStack> itemStackSupplier,
                      ManagerType type) {
        this.pos = NonNullLazy.of(blockPosSupplier);
        this.itemStackSupplier = itemStackSupplier;
        this.type = type;
    }

    @Nonnull
    @Override
    public LazyOptional<IManager> getManager(IBlockReader blockReader) {
        if (this.getManagerPos() != null) {
            if (manager == null) {
                TileEntity tileEntity = blockReader.getTileEntity(this.getManagerPos());
                if (tileEntity != null) {
                    this.manager = tileEntity.getCapability(TransportAPI.MANAGER);
                    if (this.manager.isPresent()) {
                        this.manager.addListener(this::invalidatedManager);
                        this.managerPos = manager.map(IManager::getPosition)
                                .orElseGet(() -> BlockPos.ZERO);
                    }
                }
            }
        }
        return manager != null ? manager : LazyOptional.empty();
    }

    @Override
    @Nullable
    public BlockPos getManagerPos() {
        return managerPos != null && managerPos != BlockPos.ZERO ? managerPos : null;
    }

    @Override
    public void setManager(@Nonnull LazyOptional<IManager> manager) {
        this.manager = manager;
    }

    @Override
    public boolean isValidMaster(@Nonnull IManager manager) {
        return type == manager.getType();
    }

    @Override
    @Nonnull
    public ManagedObject createManagedObject() {
        return new ManagedObject(this.pos.get(), this.itemStackSupplier.get());
    }

    public void invalidatedManager(LazyOptional<IManager> optional) {
        this.manager = null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (this.getManagerPos() != null) {
            nbt.putLong("managerPos", this.getManagerPos().toLong());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("managerPos")) {
            this.managerPos = BlockPos.fromLong(nbt.getLong("managerPos"));
        }
    }
}

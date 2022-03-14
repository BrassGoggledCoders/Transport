package xyz.brassgoggledcoders.transport.blockentity.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;

import javax.annotation.Nonnull;

public abstract class BlockEntityCapabilityStorage<T, U extends T> extends BlockEntity {
    private final LazyOptional<T> lazyOptional;
    private final NonNullLazy<U> storage;

    protected BlockEntityCapabilityStorage(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.lazyOptional = LazyOptional.of(this::getStorage);
        this.storage = NonNullLazy.of(this::createStorage);
    }

    public abstract Capability<T> getCapability();

    @Nonnull
    public abstract U createStorage();

    @Nonnull
    public U getStorage() {
        return storage.get();
    }

    public abstract int getAnalogOutputSignal();

    public abstract CompoundTag saveStorage();

    public abstract void loadStorage(CompoundTag compoundTag);

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyOptional.invalidate();
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.loadStorage(nbt.getCompound("storage"));
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("storage", this.saveStorage());
    }
}

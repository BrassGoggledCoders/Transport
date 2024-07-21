package xyz.brassgoggledcoders.transport.blockentity.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.EntityCapability;

import javax.annotation.Nonnull;

public abstract class CapabilityStorageBlockEntity<T, U extends T> extends BlockEntity {
    private final U storage;

    protected CapabilityStorageBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.storage = this.createStorage();
    }

    public abstract EntityCapability<T, Direction> getCapability();

    @Nonnull
    public abstract U createStorage();

    @Nonnull
    public U getStorage() {
        return storage;
    }

    public abstract int getAnalogOutputSignal();

    public abstract CompoundTag saveStorage();

    public abstract void loadStorage(CompoundTag compoundTag);

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

    public InteractionResult use(Player pPlayer, InteractionHand pHand) {
        return InteractionResult.PASS;
    }
}

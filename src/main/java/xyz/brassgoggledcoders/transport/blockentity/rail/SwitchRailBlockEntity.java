package xyz.brassgoggledcoders.transport.blockentity.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SwitchRailBlockEntity extends BlockEntity {
    private long lastHitGameTime = -1;

    public SwitchRailBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public void setLastHit(long gameTime) {
        this.lastHitGameTime = gameTime;
    }

    public long getLastHitGameTime() {
        return this.lastHitGameTime;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putLong("LastHitGameTime", lastHitGameTime);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        this.lastHitGameTime = pTag.getLong("LastHitGameTime");
    }
}

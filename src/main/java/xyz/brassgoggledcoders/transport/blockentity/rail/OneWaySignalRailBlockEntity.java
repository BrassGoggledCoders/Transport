package xyz.brassgoggledcoders.transport.blockentity.rail;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.signal.SignalBlock;
import xyz.brassgoggledcoders.transport.signal.SignalLevelData;
import xyz.brassgoggledcoders.transport.signal.SignalPoint;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class OneWaySignalRailBlockEntity extends BlockEntity {
    public static final RegistryEntry<BlockEntityType<OneWaySignalRailBlockEntity>> TYPE =
            TransportBlocks.ONE_WAY_SIGNAL_RAIL
                    .getSibling(ForgeRegistries.BLOCK_ENTITIES);
    private SignalPoint signalPoint;
    private Set<SignalBlock> forwardBlocks;
    private UUID signalPointUUID;

    public OneWaySignalRailBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public OneWaySignalRailBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(TYPE.get(), pWorldPosition, pBlockState);
    }

    public void onMinecartPass(AbstractMinecart minecart) {
        SignalPoint thisSignalPoint = this.getSignalPoint();
        if (thisSignalPoint != null) {
            SignalLevelData.getFor(this.getLevel())
                    .ifPresent(signalLevelData -> signalLevelData.onEntityPass(minecart, thisSignalPoint));
        }

    }

    @Nullable
    public SignalPoint getSignalPoint() {
        if (this.signalPoint == null) {
            if (this.getLevel() instanceof ServerLevel serverLevel) {
                if (this.signalPointUUID == null) {
                    this.signalPoint = SignalLevelData.getFor(serverLevel)
                            .createSignalPoint(
                                    this.getBlockState().getBlock(),
                                    this.getBlockPos()
                            );
                    this.signalPointUUID = this.signalPoint.uuid();
                } else {
                    this.signalPoint = SignalLevelData.getFor(serverLevel)
                            .getSignalPointByUUID(this.signalPointUUID)
                            .orElse(null);
                }
            }
        }
        return this.signalPoint;
    }

    @NotNull
    public Set<SignalBlock> getForwardBlocks() {
        if (this.forwardBlocks == null) {
            this.forwardBlocks = SignalLevelData.getFor(this.getLevel())
                    .flatMap(signalLevelData -> Optional.ofNullable(this.getSignalPoint())
                            .map(signalLevelData::getForwardSignalBlocks)
                    )
                    .orElse(Collections.emptySet());
        }
        return this.forwardBlocks;
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("SignalPointUUID")) {
            this.signalPointUUID = pTag.getUUID("SignalPointUUID");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.signalPointUUID != null) {
            pTag.putUUID("SignalPointUUID", this.signalPointUUID);
        }
    }
}

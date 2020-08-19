package xyz.brassgoggledcoders.transport.tileentity.rail;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.transport.block.rail.HoldingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.TimedHoldingRailBlock;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.util.TickTimer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.UUID;

public class TimedHoldingRailTileEntity extends TileEntity implements ITickableTileEntity {

    private final Map<UUID, TickTimer> timers;

    public TimedHoldingRailTileEntity() {
        this(TransportBlocks.TIMED_HOLDING_RAIL.getTileEntityType());
    }

    public TimedHoldingRailTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.timers = Maps.newHashMap();
    }

    public void onMinecartPass(AbstractMinecartEntity minecartEntity) {
        TickTimer timer = this.timers.get(minecartEntity.getUniqueID());
        if (timer == null) {
            timer = new TickTimer(5);
            this.timers.put(minecartEntity.getUniqueID(), timer);
        }
        timer.resetTimeToLive();
        if (timer.getNumberOfTicks() > this.getMaxWait()) {
            HoldingRailBlock.handleGo(this.getBlockState(), minecartEntity);
        } else {
            HoldingRailBlock.handleStop(minecartEntity);
        }
    }

    @Override
    public void tick() {
        boolean powered = this.getBlockState().get(HoldingRailBlock.POWERED);
        this.timers.entrySet().removeIf(entry -> !entry.getValue().tick(!powered));
    }

    private double getMaxWait() {
        return Math.pow(10, this.getBlockState().get(TimedHoldingRailBlock.DELAY));
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        CompoundNBT superNBT = super.write(nbt);
        ListNBT timerListNBT = new ListNBT();
        for (Map.Entry<UUID, TickTimer> entry : timers.entrySet()) {
            CompoundNBT timerNBT = entry.getValue().serializeNBT();
            timerNBT.putUniqueId("uuid", entry.getKey());
            timerListNBT.add(timerNBT);
        }
        superNBT.put("timers", timerListNBT);
        return superNBT;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        ListNBT timerListNBT = nbt.getList("timers", Constants.NBT.TAG_COMPOUND);
        this.timers.clear();
        for (int i = 0; i < timerListNBT.size(); i++) {
            CompoundNBT timerNBT = timerListNBT.getCompound(i);
            this.timers.put(timerNBT.getUniqueId("uuid"), new TickTimer(timerNBT));
        }
    }
}

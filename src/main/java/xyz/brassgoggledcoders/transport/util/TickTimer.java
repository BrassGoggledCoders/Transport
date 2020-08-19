package xyz.brassgoggledcoders.transport.util;

import net.minecraft.nbt.CompoundNBT;

public class TickTimer {
    private final int maxTimeToLive;
    private int timeToLive;
    private int numberOfTicks;

    public TickTimer(int timeToLive) {
        this.timeToLive = timeToLive;
        this.maxTimeToLive = timeToLive;
    }

    public TickTimer(CompoundNBT nbt) {
        this(nbt.getInt("maxTimeToLive"));
        this.timeToLive = nbt.getInt("timeToLive");
        this.numberOfTicks = nbt.getInt("numberOfTicks");
    }

    public boolean tick(boolean increaseNumberOfTicks) {
        if (increaseNumberOfTicks) {
            ++this.numberOfTicks;
        }
        --this.timeToLive;
        return this.timeToLive > 0;
    }

    public void resetTimeToLive() {
        this.timeToLive = maxTimeToLive;
    }

    public long getNumberOfTicks() {
        return numberOfTicks;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("maxTimeToLive", this.maxTimeToLive);
        compoundNBT.putInt("timeToLive", this.timeToLive);
        compoundNBT.putInt("numberOfTicks", this.numberOfTicks);
        return compoundNBT;
    }
}

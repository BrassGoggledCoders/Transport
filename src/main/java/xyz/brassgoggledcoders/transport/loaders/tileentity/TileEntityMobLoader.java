package xyz.brassgoggledcoders.transport.loaders.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import xyz.brassgoggledcoders.transport.api.capability.mobcarrier.CapabilityMob;
import xyz.brassgoggledcoders.transport.api.capability.mobcarrier.IMobCarrier;
import xyz.brassgoggledcoders.transport.api.capability.mobcarrier.MobCarrier;
import xyz.brassgoggledcoders.transport.library.tileentity.loader.TileEntityLoaderBase;

public class TileEntityMobLoader extends TileEntityLoaderBase<IMobCarrier> {
    private MobCarrier mobCarrier;

    public TileEntityMobLoader() {
        mobCarrier = new MobCarrier();
    }

    @Override
    protected void readCapability(NBTTagCompound data) {

    }

    @Override
    protected void writeCapability(NBTTagCompound data) {

    }

    @Override
    protected Capability<IMobCarrier> getCapabilityType() {
        return CapabilityMob.CARRIER;
    }

    @Override
    protected IMobCarrier getInternalCapability() {
        return mobCarrier;
    }

    @Override
    protected IMobCarrier getOutputCapability() {
        return mobCarrier;
    }

    @Override
    protected IMobCarrier getInputCapability() {
        return mobCarrier;
    }

    @Override
    protected boolean transfer(IMobCarrier from, IMobCarrier to) {
        return false;
    }
}

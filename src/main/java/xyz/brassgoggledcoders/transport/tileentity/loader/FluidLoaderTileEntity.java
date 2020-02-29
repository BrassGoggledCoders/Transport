package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.List;

public class FluidLoaderTileEntity extends BasicLoaderTileEntity<IFluidHandler> {
    public FluidLoaderTileEntity() {
        super(TransportBlocks.FLUID_LOADER.getTileEntityType(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
    }

    @Override
    protected void transfer(IFluidHandler from, IFluidHandler to) {

    }

    @Override
    protected LazyOptional<IFluidHandler> getInternalCAP() {
        return null;
    }

    @Override
    protected LazyOptional<IFluidHandler> createOutputCAP() {
        return null;
    }

    @Override
    protected LazyOptional<IFluidHandler> createInputCAP() {
        return null;
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return null;
    }
}

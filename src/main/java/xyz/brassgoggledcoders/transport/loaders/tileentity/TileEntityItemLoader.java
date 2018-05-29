package xyz.brassgoggledcoders.transport.loaders.tileentity;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.library.tileentity.loader.TileEntityLoaderBase;

public class TileEntityItemLoader extends TileEntityLoaderBase<IItemHandler> {
    @Override
    public Capability<?> getCapabilityType() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T castCapability(IItemHandler iItemHandler) {
        return null;
    }

    @Override
    public IItemHandler getInternalCapability() {
        return null;
    }

    @Override
    public IItemHandler getOutputCapability() {
        return null;
    }

    @Override
    public IItemHandler getInputCapability() {
        return null;
    }
}

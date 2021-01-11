package xyz.brassgoggledcoders.transport.capability.itemhandler.furnaceminecart;

import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FurnaceMinecartFuelProvider implements ICapabilityProvider {
    private final FurnaceMinecartFuelHandler furnaceMinecartFuelHandler;
    private final LazyOptional<IItemHandler> lazyOptional;

    public FurnaceMinecartFuelProvider(FurnaceMinecartEntity furnaceMinecartEntity) {
        this.furnaceMinecartFuelHandler = new FurnaceMinecartFuelHandler(furnaceMinecartEntity);
        this.lazyOptional = LazyOptional.of(() -> furnaceMinecartFuelHandler);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, lazyOptional);
    }

    public void invalidate() {
        lazyOptional.invalidate();
    }
}

package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import xyz.brassgoggledcoders.transport.capability.FluidHandlerDirectional;
import xyz.brassgoggledcoders.transport.util.TransferUtils;

import java.util.List;

public class FluidLoaderTileEntity extends BasicLoaderTileEntity<IFluidHandler> {
    private final FluidTankComponent<FluidLoaderTileEntity> fluidTankComponent;
    private final LazyOptional<IFluidHandler> lazyFluid;

    public FluidLoaderTileEntity(TileEntityType<? extends FluidLoaderTileEntity> tileEntityType) {
        super(tileEntityType, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, TransferUtils::transferFluid);
        this.fluidTankComponent = new FluidTankComponent<>("Loader", 10 * FluidAttributes.BUCKET_VOLUME,
                80, 28);
        this.lazyFluid = LazyOptional.of(() -> fluidTankComponent);
    }

    @Override
    public void onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (this.getTheWorld().isRemote || !FluidUtil.interactWithFluidHandler(player, hand, fluidTankComponent)) {
            super.onActivated(player, hand, rayTraceResult);
        }
    }

    @Override
    protected LazyOptional<IFluidHandler> getInternalCAP() {
        return lazyFluid;
    }

    @Override
    protected LazyOptional<IFluidHandler> createOutputCAP() {
        return LazyOptional.of(() -> new FluidHandlerDirectional(fluidTankComponent, false));
    }

    @Override
    protected LazyOptional<IFluidHandler> createInputCAP() {
        return LazyOptional.of(() -> new FluidHandlerDirectional(fluidTankComponent, true));
    }

    @Override
    protected CompoundNBT serializeCap() {
        return fluidTankComponent.writeToNBT(new CompoundNBT());
    }

    @Override
    protected void deserializeCap(CompoundNBT compoundNBT) {
        fluidTankComponent.readFromNBT(compoundNBT);
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return fluidTankComponent.getScreenAddons();
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return fluidTankComponent.getContainerAddons();
    }
}

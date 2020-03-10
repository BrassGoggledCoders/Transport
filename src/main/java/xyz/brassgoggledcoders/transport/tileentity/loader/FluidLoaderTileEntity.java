package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import xyz.brassgoggledcoders.transport.capability.FluidHandlerDirectional;
import xyz.brassgoggledcoders.transport.capability.FluidTankPlusComponent;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddon;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.Collections;
import java.util.List;

public class FluidLoaderTileEntity extends BasicLoaderTileEntity<IFluidHandler> {
    private final FluidTankPlusComponent<FluidLoaderTileEntity> fluidTankComponent;
    private final LazyOptional<IFluidHandler> lazyFluid;

    public FluidLoaderTileEntity() {
        super(TransportBlocks.FLUID_LOADER.getTileEntityType(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        this.fluidTankComponent = new FluidTankPlusComponent<>("Loader", 10000, 80, 28);
        this.lazyFluid = LazyOptional.of(() -> fluidTankComponent);
    }

    @Override
    protected void transfer(IFluidHandler from, IFluidHandler to) {
        FluidStack output = from.drain(1000, FluidAction.SIMULATE);
        if (!output.isEmpty()) {
            int filledAmount = to.fill(output, FluidAction.SIMULATE);
            if (filledAmount > 0) {
                to.fill(from.drain(filledAmount, FluidAction.EXECUTE), FluidAction.EXECUTE);
            }
        }
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
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return fluidTankComponent.getScreenAddons();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        fluidTankComponent.readFromNBT(nbt.getCompound("tank"));
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("tank", fluidTankComponent.writeToNBT(new CompoundNBT()));
        return nbt;
    }

    @Override
    public List<IContainerAddon> getContainerAddons() {
        return Collections.singletonList(fluidTankComponent);
    }
}

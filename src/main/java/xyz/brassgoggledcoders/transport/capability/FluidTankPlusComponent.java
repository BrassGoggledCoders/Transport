package xyz.brassgoggledcoders.transport.capability;

import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import net.minecraft.util.IIntArray;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddon;
import xyz.brassgoggledcoders.transport.container.tracked.FluidReferenceHolder;

import java.util.Collections;
import java.util.List;

public class FluidTankPlusComponent<T extends IComponentHarness> extends FluidTankComponent<T> implements IContainerAddon {
    public FluidTankPlusComponent(String name, int amount, int posX, int posY) {
        super(name, amount, posX, posY);
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluid = fluidStack;
    }

    @Override
    public List<IIntArray> getTrackedIntArrays() {
        return Collections.singletonList(new FluidReferenceHolder(this));
    }
}

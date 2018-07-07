package xyz.brassgoggledcoders.transport.basiccargo;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CargoBasic;

@Module(Transport.ID)
public class BasicCargoModule extends ModuleBase {
    @Override
    public String getName() {
        return "Basic Cargo";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        TransportAPI.getCargoRegistry().addEntries(
                new CargoBasic<>(new ResourceLocation(Transport.ID, "energy_loader"), CapabilityEnergy.ENERGY, new EnergyStorage(100000)),
                new CargoBasic<>(new ResourceLocation(Transport.ID, "item_loader"), CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new ItemStackHandler(27)),
                new CargoBasic<>(new ResourceLocation(Transport.ID, "fluid_loader"), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, new FluidTank(16))
        );
    }
}

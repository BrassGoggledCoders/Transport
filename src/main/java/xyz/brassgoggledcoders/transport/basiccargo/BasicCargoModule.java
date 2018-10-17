package xyz.brassgoggledcoders.transport.basiccargo;

import com.teamacronymcoders.base.capability.energy.EnergyStorageSerializable;
import com.teamacronymcoders.base.capability.fluid.FluidTankSerializable;
import com.teamacronymcoders.base.modularguisystem.GuiBuilder;
import com.teamacronymcoders.base.modularguisystem.components.inventory.InventoryGuiComponent;
import com.teamacronymcoders.base.modularguisystem.components.inventory.PlayerInventoryGuiComponent;
import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CargoBasic;
import xyz.brassgoggledcoders.transport.api.cargo.CargoRegistry;
import xyz.brassgoggledcoders.transport.api.cargo.ICargo;
import xyz.brassgoggledcoders.transport.api.registry.TransportRegisterEvent;

@Module(Transport.ID)
public class BasicCargoModule extends ModuleBase {
    @Override
    public String getName() {
        return "Basic Cargo";
    }

    @Override
    public boolean hasEventsHandlers() {
        return true;
    }

    @SubscribeEvent
    public void registerCargo(TransportRegisterEvent<CargoRegistry> cargoRegisterEvent) {
        cargoRegisterEvent.getRegistry().addEntries(
                new CargoBasic<>(new ResourceLocation(Transport.ID, "fe_loader"), CapabilityEnergy.ENERGY, new EnergyStorageSerializable(100000, 10000)),
                new CargoBasic<>(new ResourceLocation(Transport.ID, "item_loader"), CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new ItemStackHandler(9)),
                new CargoBasic<>(new ResourceLocation(Transport.ID, "fluid_loader"), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, new FluidTankSerializable(16000))
        );
    }
}

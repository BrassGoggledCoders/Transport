package xyz.brassgoggledcoders.transport.content;

import net.minecraft.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.EnergyCargoModuleInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.FluidCargoModuleInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.ItemCargoModuleInstance;

@SuppressWarnings("unused")
public class TransportCargoModules {
    private static final DeferredRegister<CargoModule> CARGO = DeferredRegister.create(CargoModule.class, Transport.ID);

    public static final RegistryObject<CargoModule> EMPTY = CARGO.register("empty", () -> new CargoModule(
            () -> Blocks.AIR));
    public static final RegistryObject<CargoModule> ITEM = CARGO.register("item", () -> new CargoModule(
            TransportBlocks.ITEM_LOADER::getBlock, ItemCargoModuleInstance::new));
    public static final RegistryObject<CargoModule> ENERGY = CARGO.register("energy", () -> new CargoModule(
            TransportBlocks.ENERGY_LOADER::getBlock, EnergyCargoModuleInstance::new));
    public static final RegistryObject<CargoModule> FLUID = CARGO.register("fluid", () -> new CargoModule(
            TransportBlocks.FLUID_LOADER::getBlock, FluidCargoModuleInstance::new));

    public static void register(IEventBus eventBus) {
        CARGO.register(eventBus);
    }
}

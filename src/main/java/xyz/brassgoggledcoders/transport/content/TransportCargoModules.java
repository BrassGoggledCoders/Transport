package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.EnergyCargoModuleInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.FluidCargoModuleInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.ItemCargoModuleInstance;

@SuppressWarnings("unused")
public class TransportCargoModules {
    public static final RegistryEntry<CargoModule> EMPTY = Transport.getRegistrate()
            .object("empty")
            .cargoModule(() -> Blocks.AIR, CargoModuleInstance::new)
            .lang("Empty")
            .register();

    public static final RegistryEntry<CargoModule> ITEM = Transport.getRegistrate()
            .object("item")
            .cargoModule(TransportBlocks.ITEM_LOADER, ItemCargoModuleInstance::new)
            .lang("Inventory")
            .register();

    public static final RegistryEntry<CargoModule> ENERGY = Transport.getRegistrate()
            .object("energy")
            .cargoModule(TransportBlocks.ENERGY_LOADER, EnergyCargoModuleInstance::new)
            .lang("Energy Storage")
            .register();

    public static final RegistryEntry<CargoModule> FLUID = Transport.getRegistrate()
            .object("fluid")
            .cargoModule(TransportBlocks.FLUID_LOADER, FluidCargoModuleInstance::new)
            .lang("Fluid Tank")
            .register();

    public static final RegistryEntry<CargoModule> PODIUM = Transport.getRegistrate()
            .object("podium")
            .cargoModule(TransportBlocks.PODIUM, CargoModuleInstance::new, true)
            .register();

    public static void setup() {
    }
}

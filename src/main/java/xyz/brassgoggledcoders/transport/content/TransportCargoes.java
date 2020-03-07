package xyz.brassgoggledcoders.transport.content;

import net.minecraft.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargo.EmptyCargo;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.EnergyCargoInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.FluidCargoInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.ItemCargoInstance;

@SuppressWarnings("unused")
public class TransportCargoes {
    private static final DeferredRegister<Cargo> CARGO = new DeferredRegister<>(TransportAPI.CARGO, Transport.ID);

    public static final RegistryObject<Cargo> EMPTY = CARGO.register("empty", EmptyCargo::new);
    public static final RegistryObject<Cargo> ITEM = CARGO.register("item", () -> new Cargo(
            TransportBlocks.ITEM_LOADER::getBlock, ItemCargoInstance::new));
    public static final RegistryObject<Cargo> ENERGY = CARGO.register("energy", () -> new Cargo(
            TransportBlocks.ENERGY_LOADER::getBlock, EnergyCargoInstance::new));
    public static final RegistryObject<Cargo> FLUID = CARGO.register("fluid", () -> new Cargo(
            TransportBlocks.FLUID_LOADER::getBlock, FluidCargoInstance::new));

    public static void register(IEventBus eventBus) {
        CARGO.register(eventBus);
    }
}

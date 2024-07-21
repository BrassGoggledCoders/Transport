package xyz.brassgoggledcoders.transport.api.capability;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class TransportCapabilities {
    public static final ShellContentCapability<IItemHandler, Direction> ITEM_HANDLER = ShellContentCapability.createSided(
            new ResourceLocation("neoforge", "item_handler"),
            IItemHandler.class,
            ItemHandler.ENTITY_AUTOMATION
    );

    public static final ShellContentCapability<IFluidHandler, Direction> FLUID_HANDLER = ShellContentCapability.createSided(
            new ResourceLocation("neoforge", "fluid_handler"),
            IFluidHandler.class,
            Capabilities.FluidHandler.ENTITY
    );

    public static final ShellContentCapability<IEnergyStorage, Direction> ENERGY_STORAGE = ShellContentCapability.createSided(
            new ResourceLocation("neoforge", "energy"),
            IEnergyStorage.class,
            EnergyStorage.ENTITY
    );
}

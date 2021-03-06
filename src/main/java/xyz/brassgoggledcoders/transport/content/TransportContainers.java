package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.builders.ContainerBuilder;
import com.tterrag.registrate.util.entry.ContainerEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.loader.EnergyLoaderContainer;
import xyz.brassgoggledcoders.transport.container.loader.FluidLoaderContainer;
import xyz.brassgoggledcoders.transport.container.loader.ItemLoaderContainer;
import xyz.brassgoggledcoders.transport.container.locomotive.SteamLocomotiveContainer;
import xyz.brassgoggledcoders.transport.container.module.VehicleModuleContainer;
import xyz.brassgoggledcoders.transport.container.module.engine.SolidFuelModuleContainer;
import xyz.brassgoggledcoders.transport.container.moduleconfigurator.ModuleConfiguratorContainer;
import xyz.brassgoggledcoders.transport.container.navigation.NavigationChartContainer;
import xyz.brassgoggledcoders.transport.screen.ModuleConfiguratorScreen;
import xyz.brassgoggledcoders.transport.screen.loader.EnergyLoaderScreen;
import xyz.brassgoggledcoders.transport.screen.loader.FluidLoaderScreen;
import xyz.brassgoggledcoders.transport.screen.loader.ItemLoaderScreen;
import xyz.brassgoggledcoders.transport.screen.locomotive.SteamLocomotiveScreen;
import xyz.brassgoggledcoders.transport.screen.module.VehicleModuleScreen;
import xyz.brassgoggledcoders.transport.screen.module.engine.SolidFuelModuleScreen;
import xyz.brassgoggledcoders.transport.screen.navigation.NavigationChartScreen;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class TransportContainers {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, Transport.ID);

    public static final ContainerEntry<VehicleModuleContainer> MODULE =
            Transport.getRegistrate()
                    .object("module")
                    .container(VehicleModuleContainer::new, () -> VehicleModuleScreen::new)
                    .register();

    public static final ContainerEntry<ModuleConfiguratorContainer> MODULE_CONFIGURATOR =
            Transport.getRegistrate()
                    .object("module_configurator")
                    .container(new ContainerBuilder.ContainerFactory<ModuleConfiguratorContainer>() {
                        @Override
                        @Nonnull
                        @ParametersAreNonnullByDefault
                        public ModuleConfiguratorContainer create(ContainerType<ModuleConfiguratorContainer> containerType,
                                                                  int i, PlayerInventory playerInventory) {
                            return new ModuleConfiguratorContainer(containerType, i, playerInventory);
                        }
                    }, () -> ModuleConfiguratorScreen::new)
                    .register();

    public static final RegistryEntry<ContainerType<NavigationChartContainer>> NAVIGATION_CHART =
            Transport.getRegistrate()
                    .object("navigation_chart")
                    .container(new ContainerBuilder.ContainerFactory<NavigationChartContainer>() {
                        @Override
                        @Nonnull
                        @ParametersAreNonnullByDefault
                        public NavigationChartContainer create(ContainerType<NavigationChartContainer> containerType,
                                                               int i, PlayerInventory playerInventory) {
                            return new NavigationChartContainer(containerType, i);
                        }
                    }, () -> NavigationChartScreen::new)
                    .register();

    public static final ContainerEntry<SteamLocomotiveContainer> STEAM_LOCOMOTIVE =
            Transport.getRegistrate()
                    .object("steam_locomotive")
                    .container(SteamLocomotiveContainer::new, () -> SteamLocomotiveScreen::new)
                    .register();

    public static final ContainerEntry<SolidFuelModuleContainer> SOLID_FUEL_MODULE =
            Transport.getRegistrate()
                    .object("solid_fuel")
                    .container(SolidFuelModuleContainer::new, () -> SolidFuelModuleScreen::new)
                    .register();

    public static final ContainerEntry<ItemLoaderContainer> ITEM_LOADER =
            Transport.getRegistrate()
                    .object("item_loader")
                    .container(ItemLoaderContainer::new, () -> ItemLoaderScreen::new)
                    .register();

    public static final ContainerEntry<FluidLoaderContainer> FLUID_LOADER =
            Transport.getRegistrate()
                    .object("fluid_loader")
                    .container(FluidLoaderContainer::new, () -> FluidLoaderScreen::new)
                    .register();

    public static final ContainerEntry<EnergyLoaderContainer> ENERGY_LOADER =
            Transport.getRegistrate()
                    .object("energy_loader")
                    .container(EnergyLoaderContainer::new, () -> EnergyLoaderScreen::new)
                    .register();

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}

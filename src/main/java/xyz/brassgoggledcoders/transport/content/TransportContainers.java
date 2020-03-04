package xyz.brassgoggledcoders.transport.content;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.cargo.CargoContainer;
import xyz.brassgoggledcoders.transport.container.loader.LoaderContainer;

public class TransportContainers {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS =
            new DeferredRegister<>(ForgeRegistries.CONTAINERS, Transport.ID);

    public static final RegistryObject<ContainerType<LoaderContainer>> LOADER = CONTAINERS.register("loader",
            () -> IForgeContainerType.create(LoaderContainer::create));

    public static final RegistryObject<ContainerType<CargoContainer>> CARGO = CONTAINERS.register("cargo",
            () -> IForgeContainerType.create(CargoContainer::create));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}

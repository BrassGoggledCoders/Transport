package xyz.brassgoggledcoders.transport.content;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IEnergyStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IFluidStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IItemStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.empty.EmptyShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.energy.EnergyStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.fluid.FluidStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.item.ItemStorageShellContentCreator;

@SuppressWarnings("unused")
public class TransportShellContent {

    public static ResourceKey<Registry<Codec<? extends IShellContentCreator<?>>>> REGISTRY_KEY = ResourceKey.createRegistryKey(
            Transport.rl("shell_content")
    );

    public static DeferredRegister<Codec<? extends IShellContentCreator<?>>> DEFERRED_REGISTER = DeferredRegister.create(
            REGISTRY_KEY,
            Transport.ID
    );

    public static Registry<Codec<? extends IShellContentCreator<?>>> SHELL_CONTENT_TYPES =
            DEFERRED_REGISTER.makeRegistry(registryBuilder -> {
            });

    public static IRegisteringEntry<Codec<EmptyShellContentCreator>, Codec<? extends IShellContentCreator<?>>> EMPTY =
            Transport.getRegistering()
                    .object("empty")
                    .simple(REGISTRY_KEY, () -> EmptyShellContentCreator.CODEC);

    public static IRegisteringEntry<Codec<FluidStorageShellContentCreator>, Codec<? extends IShellContentCreator<?>>> FLUID_STORAGE =
            Transport.getRegistering()
                    .object("fluid_storage")
                    .simple(REGISTRY_KEY, () -> FluidStorageShellContentCreator.CODEC);

    public static IRegisteringEntry<Codec<ItemStorageShellContentCreator>, Codec<? extends IShellContentCreator<?>>> ITEM_STORAGE =
            Transport.getRegistering()
                    .object("item_storage")
                    .simple(REGISTRY_KEY, () -> ItemStorageShellContentCreator.CODEC);

    public static IRegisteringEntry<Codec<EnergyStorageShellContentCreator>, Codec<? extends IShellContentCreator<?>>> ENERGY_STORAGE =
            Transport.getRegistering()
                    .object("energy_storage")
                    .simple(REGISTRY_KEY, () -> EnergyStorageShellContentCreator.CODEC);

    public static void setup(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}

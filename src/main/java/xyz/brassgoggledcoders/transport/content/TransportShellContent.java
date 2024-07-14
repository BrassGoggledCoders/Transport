package xyz.brassgoggledcoders.transport.content;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IEnergyStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IFluidStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IItemStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.empty.EmptyShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.energy.EnergyStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.fluid.FluidStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.item.ItemStorageShellContentCreator;

public class TransportShellContent {

    public static DeferredRegister<Codec<? extends IShellContentCreator<?>>> DEFERRED_REGISTER = DeferredRegister.create(
            Transport.rl("shell_content"),
            Transport.ID
    );

    public static Registry<Codec<? extends IShellContentCreator<?>>> SHELL_CONTENT_TYPES =
            DEFERRED_REGISTER.makeRegistry(registryBuilder -> {});

    public static RegisteringEntry<Codec<EmptyShellContentCreator>, Codec<? extends IShellContentCreator<?>>> EMPTY = Transport.getRegistrate()
            .object("empty")
            .simple(SHELL_CONTENT_TYPES, () -> EmptyShellContentCreator.CODEC);

    public static RegistryEntry<Codec<IFluidStorageShellContentCreator<?>>> FLUID_STORAGE = Transport.getRegistrate()
            .object("fluid_storage")
            .simple(SHELL_CONTENT_TYPES, () -> FluidStorageShellContentCreator.CODEC);

    public static RegistryEntry<Codec<IItemStorageShellContentCreator<?>>> ITEM_STORAGE =
            Transport.getRegistrate()
                    .object("item_storage")
                    .simple(SHELL_CONTENT_TYPES, () -> ItemStorageShellContentCreator.CODEC);

    public static RegistryEntry<Codec<IEnergyStorageShellContentCreator<?>>> ENERGY_STORAGE =
            Transport.getRegistrate()
                    .object("energy_storage")
                    .simple(SHELL_CONTENT_TYPES, () -> EnergyStorageShellContentCreator.CODEC);

    public static void setup() {

    }

    public static void newRegistry(NewRegistryEvent newRegistryEvent) {
        newRegistryEvent.register(newRegistryEvent.create());
    }
}

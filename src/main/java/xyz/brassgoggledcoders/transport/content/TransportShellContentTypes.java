package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IEnergyStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IFluidStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.IItemStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.empty.EmptyShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.energy.EnergyStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.fluid.FluidStorageShellContentCreator;
import xyz.brassgoggledcoders.transport.shellcontent.storage.item.ItemStorageShellContentCreator;

import java.util.function.Supplier;

public class TransportShellContentTypes {
    public static DeferredRegister<ShellContentType<?>> SHELL_CONTENT_TYPES_DR = DeferredRegister.create(
            TransportAPI.SHELL_CONTENT_TYPE_KEY,
            Transport.ID
    );

    @SuppressWarnings({"unused", "unchecked"})
    public static Supplier<IForgeRegistry<ShellContentType<?>>> SHELL_CONTENT_TYPES = SHELL_CONTENT_TYPES_DR.makeRegistry(
            (Class<ShellContentType<?>>) (Class<?>) ShellContentType.class,
            RegistryBuilder::new
    );

    public static RegistryEntry<ShellContentType<EmptyShellContentCreator>> EMPTY =
            Transport.getRegistrate()
                    .object("empty")
                    .simple(ShellContentType.class, () -> new ShellContentType<>(EmptyShellContentCreator.CODEC));

    public static RegistryEntry<ShellContentType<IFluidStorageShellContentCreator<?>>> FLUID_STORAGE =
            Transport.getRegistrate()
                    .object("fluid_storage")
                    .simple(ShellContentType.class, () -> new ShellContentType<>(FluidStorageShellContentCreator.CODEC));

    public static RegistryEntry<ShellContentType<IItemStorageShellContentCreator<?>>> ITEM_STORAGE =
            Transport.getRegistrate()
                    .object("item_storage")
                    .simple(ShellContentType.class, () -> new ShellContentType<>(ItemStorageShellContentCreator.CODEC));

    public static RegistryEntry<ShellContentType<IEnergyStorageShellContentCreator<?>>> ENERGY_STORAGE =
            Transport.getRegistrate()
                    .object("energy_storage")
                    .simple(
                            ShellContentType.class,
                            () -> new ShellContentType<>(EnergyStorageShellContentCreator.CODEC)
                    );

    public static void setup() {
        SHELL_CONTENT_TYPES_DR.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

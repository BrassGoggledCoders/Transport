package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;
import xyz.brassgoggledcoders.transport.shellcontent.storage.fluid.FluidStorageShellContent;
import xyz.brassgoggledcoders.transport.shellcontent.storage.fluid.FluidStorageShellContentCreator;

import java.util.function.Supplier;

public class TransportShellContentTypes {
    @SuppressWarnings("UnstableApiUsage")
    public static Supplier<IForgeRegistry<ShellContentType<?,?>>> SHELL_CONTENT_TYPES = Transport.getRegistrate()
            .makeRegistry(
                    "shell_content_type",
                    ShellContentType.class,
                    (Supplier<RegistryBuilder<ShellContentType<?, ?>>>) RegistryBuilder::new
            );

    public static RegistryEntry<ShellContentType<FluidStorageShellContentCreator, FluidStorageShellContent>> FLUID_STORAGES =
            Transport.getRegistrate()
                    .object("shell_content_type")
                    .simple(ShellContentType.class, () -> new ShellContentType<>(FluidStorageShellContentCreator.CODEC));

    public static void setup() {

    }
}

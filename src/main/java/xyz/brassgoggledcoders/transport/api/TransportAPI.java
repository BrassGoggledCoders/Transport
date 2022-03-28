package xyz.brassgoggledcoders.transport.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.api.service.IShellContentCreatorService;
import xyz.brassgoggledcoders.transport.api.service.IShellNetworkingService;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentType;

import java.util.ServiceLoader;

public class TransportAPI {
    public static final String ID = "transport-api";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final ResourceKey<Registry<ShellContentType<?,?>>> SHELL_CONTENT_TYPE_KEY = ResourceKey.createRegistryKey(
            new ResourceLocation("transport", "shell_content_type")
    );

    public static final NonNullLazy<IShellContentCreatorService> SHELL_CONTENT_CREATOR = NonNullLazy.of(() ->
             ServiceLoader.load(IShellContentCreatorService.class)
                     .findFirst()
                     .orElseThrow(() -> new IllegalStateException("Failed to find IShellContentCreatorService"))
    );

    public static final NonNullLazy<IShellNetworkingService> SHELL_NETWORKING = NonNullLazy.of(() ->
            ServiceLoader.load(IShellNetworkingService.class)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Failed to find IShellNetworkingService"))
    );
}

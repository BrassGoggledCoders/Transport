package xyz.brassgoggledcoders.transport.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.api.service.IItemHelperService;
import xyz.brassgoggledcoders.transport.api.service.IShellContentCreatorService;
import xyz.brassgoggledcoders.transport.api.service.IShellNetworkingService;

import java.util.ServiceLoader;

public class TransportAPI {
    public static final String ID = "transport";
    public static final Logger LOGGER = LogManager.getLogger("transport-api");

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

    public static final NonNullLazy<IItemHelperService> ITEM_HELPER = NonNullLazy.of(() ->
            ServiceLoader.load(IItemHelperService.class)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Failed to find IItemHelperService"))
    );

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

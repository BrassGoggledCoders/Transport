package xyz.brassgoggledcoders.transport.api;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.api.service.IItemHelperService;
import xyz.brassgoggledcoders.transport.api.service.IShellContentCreatorService;
import xyz.brassgoggledcoders.transport.api.service.IShellNetworkingService;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public class TransportAPI {
    public static final String ID = "transport";
    public static final Logger LOGGER = LogManager.getLogger("transport-api");

    public static final Supplier<IShellContentCreatorService> SHELL_CONTENT_CREATOR = Suppliers.memoize(() ->
            ServiceLoader.load(IShellContentCreatorService.class)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Failed to find IShellContentCreatorService"))
    );

    public static final Supplier<IShellNetworkingService> SHELL_NETWORKING = Suppliers.memoize(() ->
            ServiceLoader.load(IShellNetworkingService.class)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Failed to find IShellNetworkingService"))
    );

    public static final Supplier<IItemHelperService> ITEM_HELPER = Suppliers.memoize(() ->
            ServiceLoader.load(IItemHelperService.class)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Failed to find IItemHelperService"))
    );

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}

package xyz.brassgoggledcoders.transport.service;

import net.minecraftforge.common.util.NonNullLazy;
import xyz.brassgoggledcoders.transport.api.service.IShellContentCreatorService;
import xyz.brassgoggledcoders.transport.api.service.IShellNetworkingService;

import java.util.ServiceLoader;

public class ServiceHolder {
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

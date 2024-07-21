package xyz.brassgoggledcoders.transport.data.dataregistering;

import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.DeferredProviderType;

public class TransportProviderTypes {
    public static final DeferredProviderType<DeferredShellContentInfoDataProvider> SHELL_CONTENT_INFO = new DeferredProviderType<>(
            (id, event, deferredActions) -> {

                DeferredShellContentInfoDataProvider dataProvider = new DeferredShellContentInfoDataProvider(event.getGenerator(), deferredActions);
                event.getGenerator()
                        .addProvider(event.includeServer(), dataProvider);
                return dataProvider;
            }
    );
}

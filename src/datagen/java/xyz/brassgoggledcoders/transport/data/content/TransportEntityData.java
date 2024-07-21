package xyz.brassgoggledcoders.transport.data.content;

import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.language.TranslationProvider;
import xyz.brassgoggledcoders.transport.content.TransportEntities;

public class TransportEntityData {
    public static void generate(DataRegistering dataRegistering) {
        dataRegistering.forEntry(TransportEntities.SHELL_MINECART)
                .withProvider(TranslationProvider.TYPE, (entry, provider) -> {
                    provider.addTranslation(
                            entry.get()
                                    .getDescriptionId(),
                            "Minecart"
                    );
                    provider.addTranslation(
                            entry.get()
                                    .getDescriptionId() + ".with",
                            "Minecart with %s"
                    );
                });
    }
}

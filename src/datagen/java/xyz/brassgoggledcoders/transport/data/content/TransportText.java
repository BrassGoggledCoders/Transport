package xyz.brassgoggledcoders.transport.data.content;

import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.language.TranslationProvider;
import xyz.brassgoggledcoders.transport.Transport;

public class TransportText {
    public static void generate(DataRegistering dataRegistering) {
        dataRegistering.doWithProvider(
                TranslationProvider.TYPE,
                provider -> provider.addTranslation("text", Transport.rl("shell_content"), "Shell Content: %s")
        );
    }
}

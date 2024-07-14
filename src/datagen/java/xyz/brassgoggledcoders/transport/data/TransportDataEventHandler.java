package xyz.brassgoggledcoders.transport.data;

import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderTypes;
import xyz.brassgoggledcoders.transport.Transport;

@EventBusSubscriber(modid = Transport.ID)
public class TransportDataEventHandler {
    public static void dataGathering(GatherDataEvent event) {
        DataRegistering dataRegistering = DataRegistering.of(Transport.ID);

        dataRegistering.doWithProvider(ProviderTypes.RECIPE, deferred -> deferred.deferred(TransportAdditionalData::vanillaRecipes));
        dataRegistering.doWithProvider(ProviderTypes.TAGS, TransportAdditionalData::vanillaItemTags);

        dataRegistering.doGeneration(event);
    }
}

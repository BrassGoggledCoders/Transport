package xyz.brassgoggledcoders.transport.data;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.data.content.*;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD)
public class TransportDataEventHandler {
    @SubscribeEvent
    public static void dataGathering(GatherDataEvent event) {
        DataRegistering dataRegistering = DataRegistering.of(Transport.ID);

        TransportAdditionalData.generate(dataRegistering);
        TransportBlockData.generate(dataRegistering);
        TransportEntityData.generate(dataRegistering);
        TransportItemData.generate(dataRegistering);
        TransportShellContentData.generate(dataRegistering);
        TransportText.generate(dataRegistering);

        dataRegistering.doGeneration(event);
    }
}

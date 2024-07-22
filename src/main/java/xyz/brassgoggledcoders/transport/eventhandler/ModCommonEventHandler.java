package xyz.brassgoggledcoders.transport.eventhandler;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;
import xyz.brassgoggledcoders.transport.api.capability.RegisterShellContentCapabilitiesEvent;
import xyz.brassgoggledcoders.transport.api.capability.ShellContentCapability;
import xyz.brassgoggledcoders.transport.api.capability.TransportCapabilities;
import xyz.brassgoggledcoders.transport.content.TransportAttachments;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportItems;
import xyz.brassgoggledcoders.transport.content.TransportShellContent;
import xyz.brassgoggledcoders.transport.network.NewGenerationClientMessage;
import xyz.brassgoggledcoders.transport.network.OpenMenuProviderServerMessage;
import xyz.brassgoggledcoders.transport.network.SyncShellContentCreatorInfoMessage;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD)
public class ModCommonEventHandler {

    @SubscribeEvent
    public static void capabilityRegister(RegisterCapabilitiesEvent event) {
        event.registerItem(
                IRailProvider.CAPABILITY,
                (itemStack, context) -> itemStack.getData(TransportAttachments.PATTERNED_RAIL_PROVIDER),
                TransportItems.PATTERNED_RAIL_LAYER
        );

        for (ShellContentCapability<?, ?> shellContentCapability : ShellContentCapability.getAll()) {
            shellContentCapability.registerEntityCapability(
                    event,
                    TransportEntities.SHELL_MINECART.get()
            );
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void sendShellCapabilityRegister(RegisterCapabilitiesEvent event) {
        ModLoader.get()
                .postEvent(new RegisterShellContentCapabilitiesEvent());
    }

    @SubscribeEvent
    public static void shellCapabilityRegister(RegisterShellContentCapabilitiesEvent event) {
        event.registerShellContent(
                TransportCapabilities.ITEM_HANDLER,
                TransportShellContent.ITEM_STORAGE.get(),
                (shellContent, context) -> shellContent.getHandler()
        );

        event.registerShellContent(
                TransportCapabilities.FLUID_HANDLER,
                TransportShellContent.FLUID_STORAGE.get(),
                (shellContent, context) -> shellContent.getHandler()
        );

        event.registerShellContent(
                TransportCapabilities.ENERGY_STORAGE,
                TransportShellContent.ENERGY_STORAGE.get(),
                (shellContent, context) -> shellContent.getEnergyStorage()
        );
    }

    @SubscribeEvent
    public static void networkRegister(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar payloadRegistrar = event.registrar(Transport.ID);

        payloadRegistrar.play(
                NewGenerationClientMessage.ID,
                NewGenerationClientMessage::decode,
                NewGenerationClientMessage::consume
        );

        payloadRegistrar.play(
                OpenMenuProviderServerMessage.ID,
                friendlyByteBuf -> new OpenMenuProviderServerMessage(),
                OpenMenuProviderServerMessage::consume
        );

        payloadRegistrar.play(
                SyncShellContentCreatorInfoMessage.ID,
                SyncShellContentCreatorInfoMessage::decode,
                SyncShellContentCreatorInfoMessage::consume
        );
    }
}

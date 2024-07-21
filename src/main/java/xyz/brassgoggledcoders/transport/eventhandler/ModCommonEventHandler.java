package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;
import xyz.brassgoggledcoders.transport.api.capability.RegisterShellContentCapabilitiesEvent;
import xyz.brassgoggledcoders.transport.content.TransportAttachments;
import xyz.brassgoggledcoders.transport.content.TransportItems;
import xyz.brassgoggledcoders.transport.network.NewGenerationClientMessage;
import xyz.brassgoggledcoders.transport.network.OpenMenuProviderServerMessage;
import xyz.brassgoggledcoders.transport.network.SyncShellContentCreatorInfoMessage;

import javax.annotation.ParametersAreNonnullByDefault;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD)
public class ModCommonEventHandler {

    @SubscribeEvent
    public static void capabilityRegister(RegisterCapabilitiesEvent event) {
        ModLoader.get().postEvent(new RegisterShellContentCapabilitiesEvent());
        event.registerItem(
                IRailProvider.CAPABILITY,
                (itemStack, context) -> itemStack.getData(TransportAttachments.PATTERNED_RAIL_PROVIDER),
                TransportItems.PATTERNED_RAIL_LAYER
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

package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;
import xyz.brassgoggledcoders.transport.api.capability.RegisterShellContentCapabilitiesEvent;
import xyz.brassgoggledcoders.transport.content.TransportAttachments;
import xyz.brassgoggledcoders.transport.content.TransportItems;

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
}

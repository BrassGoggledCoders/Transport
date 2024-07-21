package xyz.brassgoggledcoders.transport.api.capability;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public class RegisterShellContentCapabilitiesEvent extends Event implements IModBusEvent {
    public RegisterShellContentCapabilitiesEvent() {

    }

    public <T, C, SC extends ShellContent> void registerShellContent(
            ShellContentCapability<T, C> contentCapability,
            Codec<? extends IShellContentCreator<? extends SC>> codec,
            ICapabilityProvider<SC, T, C> capabilityProvider
    ) {
        contentCapability.addProvider(
                codec,
                capabilityProvider
        );
    }
}

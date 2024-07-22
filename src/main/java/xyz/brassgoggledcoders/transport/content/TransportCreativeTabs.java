package xyz.brassgoggledcoders.transport.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringEntry;
import xyz.brassgoggledcoders.transport.Transport;

public class TransportCreativeTabs {

    public static final IRegisteringEntry<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = Transport.getRegistering()
            .object("creative_tab")
            .simple(
                    Registries.CREATIVE_MODE_TAB,
                    () -> CreativeModeTab.builder()
                            .icon(TransportBlocks.SWITCH_RAIL::asItemStack)
                            .title(Component.literal("itemGroup.transport"))
                            .displayItems((displayParameters, output) -> output.accept(TransportItems.RAIL_BREAKER.asItemStack()))
                            .build()
            );

    public static void setup() {

    }
}

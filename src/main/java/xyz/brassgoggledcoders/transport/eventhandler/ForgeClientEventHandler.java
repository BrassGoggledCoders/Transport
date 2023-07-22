package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEventHandler {

    /**
     * Used to replace HasCustomInventoryScreen which expects vehicle to always have a UI which Shell Contents don't always do.
     * Used by Boats/Sky Ships/Other Vehicles which may default to having two seats
     *
     * @see net.minecraft.world.entity.HasCustomInventoryScreen
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void handleCustomScreenReplace(ScreenEvent.Opening event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && event.getNewScreen() instanceof InventoryScreen) {
            if (player.getVehicle() instanceof IShell shell && shell.getContent() instanceof MenuProvider) {
                event.setCanceled(true);
                Transport.NETWORK.sendOpenMenuProvider();
            }
        }
    }
}

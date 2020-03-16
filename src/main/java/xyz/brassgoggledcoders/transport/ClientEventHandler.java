package xyz.brassgoggledcoders.transport;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.renderer.CargoCarrierMinecartEntityRenderer;
import xyz.brassgoggledcoders.transport.screen.CargoScreen;
import xyz.brassgoggledcoders.transport.screen.LoaderScreen;

public class ClientEventHandler {
    public static void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(TransportBlocks.HOLDING_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.DIAMOND_CROSSING_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.ELEVATOR_SWITCH_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.ELEVATOR_SWITCH_SUPPORT.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.SCAFFOLDING_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.getBlock(), RenderType.getCutout());

        ScreenManager.registerFactory(TransportContainers.LOADER.get(), LoaderScreen::new);
        ScreenManager.registerFactory(TransportContainers.CARGO.get(), CargoScreen::new);

        Minecraft.getInstance().getRenderManager().register(TransportEntities.CARGO_MINECART.get(),
                new CargoCarrierMinecartEntityRenderer(Minecraft.getInstance().getRenderManager()));
    }
}

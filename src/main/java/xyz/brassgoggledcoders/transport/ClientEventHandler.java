package xyz.brassgoggledcoders.transport;

import com.hrznstudio.titanium.client.screen.container.BasicAddonScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.renderer.CargoCarrierMinecartEntityRenderer;
import xyz.brassgoggledcoders.transport.renderer.tileentity.ModuleConfiguratorTileEntityRenderer;

public class ClientEventHandler {
    public static void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(TransportBlocks.HOLDING_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.DIAMOND_CROSSING_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.ELEVATOR_SWITCH_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.ELEVATOR_SWITCH_SUPPORT.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.SCAFFOLDING_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.SWITCH_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.WYE_SWITCH_RAIL.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(TransportBlocks.BUMPER_RAIL.getBlock(), RenderType.getCutout());

        ScreenManager.registerFactory(TransportContainers.MODULE.get(), BasicAddonScreen::new);

        EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();
        rendererManager.register(TransportEntities.CARGO_MINECART.get(), new CargoCarrierMinecartEntityRenderer(rendererManager));

        ClientRegistry.bindTileEntityRenderer(TransportBlocks.MODULE_CONFIGURATOR.getTileEntityType(),
                ModuleConfiguratorTileEntityRenderer::new);
    }

    public static World getWorld() {
        return Minecraft.getInstance().world;
    }
}

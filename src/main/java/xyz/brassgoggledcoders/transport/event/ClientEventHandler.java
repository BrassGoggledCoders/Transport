package xyz.brassgoggledcoders.transport.event;

import com.hrznstudio.titanium.client.screen.container.BasicAddonScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.brassgoggledcoders.transport.api.TransportClientAPI;
import xyz.brassgoggledcoders.transport.api.renderer.CargoModuleRender;
import xyz.brassgoggledcoders.transport.api.renderer.IModuleRenderer;
import xyz.brassgoggledcoders.transport.api.renderer.ItemModuleRenderer;
import xyz.brassgoggledcoders.transport.content.*;
import xyz.brassgoggledcoders.transport.renderer.CargoCarrierMinecartEntityRenderer;
import xyz.brassgoggledcoders.transport.renderer.tileentity.ModuleConfiguratorTileEntityRenderer;
import xyz.brassgoggledcoders.transport.screen.ModuleConfiguratorScreen;
import xyz.brassgoggledcoders.transport.tileentity.ModuleConfiguratorTileEntity;

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
        RenderTypeLookup.setRenderLayer(TransportBlocks.TIMED_HOLDING_RAIL.getBlock(), RenderType.getCutout());

        ScreenManager.registerFactory(TransportContainers.MODULE.get(), BasicAddonScreen::new);
        ScreenManager.registerFactory(TransportContainers.MODULE_CONFIGURATOR.get(), ModuleConfiguratorScreen::new);

        EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();
        rendererManager.register(TransportEntities.CARGO_MINECART.get(), new CargoCarrierMinecartEntityRenderer(rendererManager));

        ClientRegistry.bindTileEntityRenderer(TransportBlocks.MODULE_CONFIGURATOR.getTileEntityType(),
                ModuleConfiguratorTileEntityRenderer::new);

        IModuleRenderer cargoModuleRender = new CargoModuleRender();
        TransportClientAPI.registerModuleRenderer(TransportCargoModules.ITEM.get(), cargoModuleRender);
        TransportClientAPI.registerModuleRenderer(TransportCargoModules.ENERGY.get(), cargoModuleRender);
        TransportClientAPI.registerModuleRenderer(TransportCargoModules.FLUID.get(), cargoModuleRender);

        IModuleRenderer itemModuleRender = new ItemModuleRenderer();
        TransportClientAPI.registerModuleRenderer(TransportEngineModules.BOOSTER.get(), itemModuleRender);
        TransportClientAPI.registerModuleRenderer(TransportEngineModules.CREATIVE.get(), itemModuleRender);
        TransportClientAPI.registerModuleRenderer(TransportEngineModules.SOLID_FUEL.get(), itemModuleRender);
    }

    public static World getWorld() {
        return Minecraft.getInstance().world;
    }
}

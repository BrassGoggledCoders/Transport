package xyz.brassgoggledcoders.transportlittlelogistics.eventhandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.transportlittlelogistics.TransportLittleLogistics;
import xyz.brassgoggledcoders.transportlittlelogistics.renderer.ShellWagonModel;
import xyz.brassgoggledcoders.transportlittlelogistics.renderer.ShellWagonRenderer;

@Mod.EventBusSubscriber(modid = TransportLittleLogistics.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEventHandler {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ShellWagonRenderer.SHELL_WAGON_LOCATION, ShellWagonModel::createBodyLayer);
    }
}

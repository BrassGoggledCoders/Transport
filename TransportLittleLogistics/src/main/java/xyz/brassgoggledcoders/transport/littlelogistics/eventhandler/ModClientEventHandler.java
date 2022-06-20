package xyz.brassgoggledcoders.transport.littlelogistics.eventhandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.transport.littlelogistics.TransportLL;
import xyz.brassgoggledcoders.transport.littlelogistics.renderer.ShellWagonModel;
import xyz.brassgoggledcoders.transport.littlelogistics.renderer.ShellWagonRenderer;

@Mod.EventBusSubscriber(modid = TransportLL.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEventHandler {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ShellWagonRenderer.SHELL_WAGON_LOCATION, ShellWagonModel::createBodyLayer);
    }
}

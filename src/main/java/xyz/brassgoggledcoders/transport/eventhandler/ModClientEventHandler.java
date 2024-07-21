package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.client.model.MinecartModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.model.patternedraillayer.PatternedRailLayerModelLoader;
import xyz.brassgoggledcoders.transport.renderer.ShellMinecartRenderer;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModClientEventHandler {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ShellMinecartRenderer.SHELL_MINECART_LOCATION, MinecartModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerModelLoader(ModelEvent.RegisterGeometryLoaders event) {
        event.register(PatternedRailLayerModelLoader.ID, new PatternedRailLayerModelLoader());
    }
}
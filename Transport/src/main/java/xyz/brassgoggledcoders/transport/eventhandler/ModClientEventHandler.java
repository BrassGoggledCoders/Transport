package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.client.model.MinecartModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
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
    public static void registerModelLoader(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(PatternedRailLayerModelLoader.ID, new PatternedRailLayerModelLoader());
    }
}
package xyz.brassgoggledcoders.transport.api;

import com.google.common.collect.Maps;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.renderer.IModuleRenderer;

import java.util.Map;

public class TransportClientAPI {
    private static final Map<Module<?>, IModuleRenderer> moduleRenderers = Maps.newHashMap();

    public static void registerModuleRenderer(Module<?> module, IModuleRenderer moduleRenderer) {
        moduleRenderers.put(module, moduleRenderer);
    }

    public static IModuleRenderer getModuleRenderer(Module<?> module) {
        return moduleRenderers.get(module);
    }
}

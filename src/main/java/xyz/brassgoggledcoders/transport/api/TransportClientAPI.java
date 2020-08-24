package xyz.brassgoggledcoders.transport.api;

import com.google.common.collect.Maps;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.api.renderer.CargoModuleRender;
import xyz.brassgoggledcoders.transport.api.renderer.IModuleRenderer;
import xyz.brassgoggledcoders.transport.api.renderer.hull.IHullRenderer;

import java.util.Map;

public class TransportClientAPI {
    private static final Map<Module<?>, IModuleRenderer> MODULE_RENDERER = Maps.newHashMap();
    private static final Map<ModuleType, IModuleRenderer> TYPE_DEFAULT = Maps.newHashMap();
    private static final Map<HullType, IHullRenderer> HULL_RENDERER = Maps.newHashMap();

    public static void registerModuleRenderer(Module<?> module, IModuleRenderer moduleRenderer) {
        MODULE_RENDERER.put(module, moduleRenderer);
    }

    public static IModuleRenderer getModuleRenderer(Module<?> module) {
        IModuleRenderer renderer = MODULE_RENDERER.get(module);
        if (renderer == null) {
            renderer = TYPE_DEFAULT.get(module.getType());
        }
        return renderer;
    }

    public static void setModuleTypeDefault(ModuleType moduleType, IModuleRenderer moduleRenderer) {
        TYPE_DEFAULT.put(moduleType, moduleRenderer);
    }
}

package xyz.brassgoggledcoders.transport.proxy;

import com.teamacronymcoders.base.util.ClassLoading;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

public class ClientProxy implements IProxy {
    @Override
    public ICargoRenderer getCargoRenderer(String classPath, Object... inputs) {
        return ClassLoading.createInstanceOf(ICargoRenderer.class, classPath, inputs);
    }
}

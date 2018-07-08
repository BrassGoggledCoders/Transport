package xyz.brassgoggledcoders.transport.proxy;

import xyz.brassgoggledcoders.transport.api.cargo.render.EmptyCargoRenderer;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

public class ServerProxy implements IProxy {
    @Override
    public ICargoRenderer getCargoRenderer(String classPath, Class[] classes, Object[] inputs) {
        return new EmptyCargoRenderer();
    }
}

package xyz.brassgoggledcoders.transport.proxy;

import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

public interface IProxy {
    ICargoRenderer getCargoRenderer(String classPath, Object... inputs);
}

package xyz.brassgoggledcoders.transport.api.cargo.render;

public interface ICargoRendererLoader {
    ICargoRenderer loadRenderer(String classPath, Class[] classes, Object[] inputs);
}

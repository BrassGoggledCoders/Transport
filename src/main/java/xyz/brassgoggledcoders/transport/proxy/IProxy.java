package xyz.brassgoggledcoders.transport.proxy;

import net.minecraft.item.Item;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

public interface IProxy {
    ICargoRenderer getCargoRenderer(String classPath, Class[] classes, Object[] inputs);

    String format(String key, Object inputs);

    void setItemRenderer(Item item, String renderString);

    void setupModelLoader();
}

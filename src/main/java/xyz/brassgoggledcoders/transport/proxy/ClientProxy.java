package xyz.brassgoggledcoders.transport.proxy;

import com.teamacronymcoders.base.util.ClassLoading;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;
import xyz.brassgoggledcoders.transport.minecart.render.TESRModelLoader;

public class ClientProxy implements IProxy {
    @Override
    public ICargoRenderer getCargoRenderer(String classPath, Class[] classes, Object[] inputs) {
        return ClassLoading.createInstanceOf(ICargoRenderer.class, classPath, classes, inputs);
    }

    @Override
    public String format(String key, Object inputs) {
        return I18n.format(key, inputs);
    }

    @Override
    public void setItemRenderer(Item item, String renderString) {
        item.setTileEntityItemStackRenderer(ClassLoading.createInstanceOf(TileEntityItemStackRenderer.class, renderString));
    }

    @Override
    public void setupModelLoader() {
        ModelLoaderRegistry.registerLoader(new TESRModelLoader());
    }
}

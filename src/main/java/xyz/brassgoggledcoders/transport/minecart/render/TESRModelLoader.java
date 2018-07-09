package xyz.brassgoggledcoders.transport.minecart.render;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import xyz.brassgoggledcoders.transport.Transport;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TESRModelLoader implements ICustomModelLoader {
    private final static TESRModel MODEL = new TESRModel();
    private final static ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(new ResourceLocation(Transport.ID, "tesr"), "inventory");

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return MODEL_LOCATION.equals(modelLocation);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) {
        return MODEL;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}

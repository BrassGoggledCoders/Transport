package xyz.brassgoggledcoders.transport.model.item;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ModularItemModelLoader implements IModelLoader<ModularItemModel> {
    private ModularItemModel model = null;
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        this.model = null;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ModularItemModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return this.getModel();
    }

    public ModularItemModel getModel() {
        if (this.model == null) {
            this.model = new ModularItemModel();
        }
        return this.model;
    }
}

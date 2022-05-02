package xyz.brassgoggledcoders.transport.model.patternedraillayer;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraftforge.client.model.IModelConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public record OverrideModelConfiguration(
        IModelConfiguration parent,
        Map<String, Material> textures
) implements IModelConfiguration {

    @Nullable
    @Override
    public UnbakedModel getOwnerModel() {
        return parent().getOwnerModel();
    }

    @Override
    @NotNull
    public String getModelName() {
        return parent().getModelName();
    }

    @Override
    public boolean isTexturePresent(@NotNull String name) {
        return textures().containsKey(name) || parent().isTexturePresent(name);
    }

    @Override
    @NotNull
    public Material resolveTexture(@NotNull String name) {
        return Optional.ofNullable(textures().get(name))
                .orElseGet(() -> parent().resolveTexture(name));
    }

    @Override
    public boolean isShadedInGui() {
        return parent().isShadedInGui();
    }

    @Override
    public boolean isSideLit() {
        return parent().isSideLit();
    }

    @Override
    public boolean useSmoothLighting() {
        return parent().useSmoothLighting();
    }

    @Override
    @NotNull
    public ItemTransforms getCameraTransforms() {
        return parent().getCameraTransforms();
    }

    @Override
    @NotNull
    public ModelState getCombinedTransform() {
        return parent().getCombinedTransform();
    }
}

package xyz.brassgoggledcoders.transport.model.item.dynamic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.util.math.vector.TransformationMatrix;

import javax.annotation.Nonnull;

public class SimpleUVModelTransform implements IModelTransform {
    private final ImmutableMap<?, TransformationMatrix> map;
    private final TransformationMatrix base;
    private final boolean uvLock;

    public SimpleUVModelTransform(ImmutableMap<?, TransformationMatrix> map, boolean uvLock) {
        this(map, TransformationMatrix.identity(), uvLock);
    }

    public SimpleUVModelTransform(ImmutableMap<?, TransformationMatrix> map, TransformationMatrix base, boolean uvLock) {
        this.map = map;
        this.base = base;
        this.uvLock = uvLock;
    }

    @Override
    public boolean isUvLock() {
        return uvLock;
    }

    @Override
    @Nonnull
    public TransformationMatrix getRotation() {
        return base;
    }

    @Override
    public TransformationMatrix getPartTransformation(Object part) {
        return map.getOrDefault(part, TransformationMatrix.identity());
    }
}

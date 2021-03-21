package xyz.brassgoggledcoders.transport.model.item.dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import xyz.brassgoggledcoders.transport.Transport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class DynamicModel<T> {
    private static final Random RAND = new Random();

    public abstract IBakedModel get(T key);

    public static DynamicModel<Void> createSimple(ResourceLocation model, String key, ModelType type) {
        return new SimpleDynamicModel(model, key, type);
    }

    public List<BakedQuad> getNullQuads(T key, BlockState state) {
        return getNullQuads(key, state, EmptyModelData.INSTANCE);
    }

    public List<BakedQuad> getNullQuads(T key, BlockState state, IModelData data) {
        return get(key).getQuads(state, null, RAND, data);
    }

    private static class SidedDynamicModel extends DynamicModel<Direction> {
        private final Map<Direction, ModelResourceLocation> names = new HashMap<>();

        private SidedDynamicModel(ResourceLocation name, String desc, ModelType type) {
            ResourceLocation baseLoc = Transport.rl("dynamic/" + desc);
            for (Direction d : Direction.values()) {
                if (d.getAxis() != Direction.Axis.Y) {
                    names.put(d, new ModelResourceLocation(baseLoc, d.getString()));
                    DynamicModelLoader.requestModel(
                            DynamicModel.getRequest(type, name, (int) d.getHorizontalAngle() + 180),
                            names.get(d));
                }

            }
        }

        @Override
        public IBakedModel get(Direction key) {
            final BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
            return blockRenderer.getBlockModelShapes().getModelManager().getModel(names.get(key));
        }
    }

    private static class SimpleDynamicModel extends DynamicModel<Void> {
        private final ModelResourceLocation name;

        private SimpleDynamicModel(ResourceLocation name, String desc, ModelType type) {
            ResourceLocation baseLoc = Transport.rl("dynamic/" + desc);
            this.name = new ModelResourceLocation(baseLoc, "");
            DynamicModelLoader.requestModel(DynamicModel.getRequest(type, name, 0), this.name);
        }

        @Override
        public IBakedModel get(Void key) {
            final BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
            return blockRenderer.getBlockModelShapes().getModelManager().getModel(name);
        }
    }

    private static DynamicModelLoader.ModelRequest getRequest(ModelType type, ResourceLocation loc, int rotY) {
        if (type == ModelType.OBJ) {
            return DynamicModelLoader.ModelRequest.obj(loc, rotY);
        }
        throw new UnsupportedOperationException();
    }

    public enum ModelType {
        OBJ
    }

}

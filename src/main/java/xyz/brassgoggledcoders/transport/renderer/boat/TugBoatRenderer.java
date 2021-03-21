package xyz.brassgoggledcoders.transport.renderer.boat;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import xyz.brassgoggledcoders.transport.entity.TugBoatEntity;
import xyz.brassgoggledcoders.transport.model.entity.HulledBoatModel;
import xyz.brassgoggledcoders.transport.model.item.dynamic.DynamicModel;

import javax.annotation.Nonnull;

public abstract class TugBoatRenderer<T extends TugBoatEntity> extends EntityRenderer<T> {
    private static final HulledBoatModel<TugBoatEntity> BOAT_MODEL = new HulledBoatModel<>(entity -> true);

    private final ResourceLocation texture;
    private final DynamicModel<Void> hull;
    private final DynamicModel<Void> leftPaddle;
    private final DynamicModel<Void> rightPaddle;

    protected TugBoatRenderer(ResourceLocation name, EntityRendererManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.8F;
        this.texture = new ResourceLocation(name.getNamespace(), "texture/entity/" + name.getPath() + ".png");
        this.hull = DynamicModel.createSimple(
                new ResourceLocation(name.getNamespace(), "item/" + name.getPath() + "_hull.obj"),
                name.getPath() + "_hull",
                DynamicModel.ModelType.OBJ
        );
        this.leftPaddle = DynamicModel.createSimple(
                new ResourceLocation(name.getNamespace(), "item/" + name.getPath() + "_left_paddle.obj"),
                name.getPath() + "_left_paddle",
                DynamicModel.ModelType.OBJ
        );
        this.rightPaddle = DynamicModel.createSimple(
                new ResourceLocation(name.getNamespace(), "item/" + name.getPath() + "_right_paddle.obj"),
                name.getPath() + "_right_paddle",
                DynamicModel.ModelType.OBJ
        );
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       @Nonnull IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.push();
        matrixStack.translate(0.0D, 0.375D, 0.0D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        float f = (float) entity.getTimeSinceHit() - partialTicks;
        float f1 = entity.getDamageTaken() - partialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            matrixStack.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(f) * f * f1 / 10.0F * (float) entity.getForwardDirection()));
        }

        float f2 = entity.getRockingAngle(partialTicks);
        if (!MathHelper.epsilonEquals(f2, 0.0F)) {
            matrixStack.rotate(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), entity.getRockingAngle(partialTicks), true));
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));

        this.renderBoat(entity, matrixStack, buffer, packedLight);

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        matrixStack.pop();
    }

    protected void renderBoat(T entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        IBakedModel hullModel = this.hull.get(null);
        IBakedModel leftPaddle = this.leftPaddle.get(null);
        IBakedModel rightPaddle = this.rightPaddle.get(null);
        if (hullModel != null) {
            matrixStack.push();
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer()
                    .renderModel(hullModel, ItemStack.EMPTY, packedLight, OverlayTexture.NO_OVERLAY, matrixStack,
                            buffer.getBuffer(RenderType.getTranslucent()));
            matrixStack.push();
            matrixStack.rotate(new Quaternion(50F,0,  0, true));
            Minecraft.getInstance().getItemRenderer()
                    .renderModel(leftPaddle, ItemStack.EMPTY, packedLight, OverlayTexture.NO_OVERLAY, matrixStack,
                            buffer.getBuffer(RenderType.getTranslucent()));
            matrixStack.pop();
            if (!entity.canSwim()) {
                IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getWaterMask());
                matrixStack.scale(0.75F, 0, 2.8F);
                matrixStack.translate(0, 0, -0.05F);
                BOAT_MODEL.noWater().render(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY);
            }
            matrixStack.pop();
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull T entity) {
        return texture;
    }
}

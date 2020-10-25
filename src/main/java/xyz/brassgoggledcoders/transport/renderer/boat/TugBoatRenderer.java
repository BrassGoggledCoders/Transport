package xyz.brassgoggledcoders.transport.renderer.boat;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import xyz.brassgoggledcoders.transport.entity.TugBoatEntity;
import xyz.brassgoggledcoders.transport.model.entity.HulledBoatModel;
import xyz.brassgoggledcoders.transport.model.item.EntityItemModelCache;
import xyz.brassgoggledcoders.transport.util.CachedValue;

import javax.annotation.Nonnull;

public class TugBoatRenderer<T extends TugBoatEntity> extends EntityRenderer<T> {
    private static final HulledBoatModel<TugBoatEntity> BOAT_MODEL = new HulledBoatModel<>(entity -> true);
    private final ResourceLocation textureLocation;
    private final CachedValue<IBakedModel> cachedBakedModel;
    private final float scale;

    public TugBoatRenderer(NonNullSupplier<? extends Item> itemSupplier, float scale, ResourceLocation textureLocation,
                           EntityRendererManager renderManager) {
        super(renderManager);
        this.cachedBakedModel = EntityItemModelCache.getBakedModelCacheFor(itemSupplier);
        this.textureLocation = textureLocation;
        this.scale = scale;
        this.shadowSize = 0.8F;
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

        this.renderBoat(entity, partialTicks, matrixStack, buffer, packedLight);

        matrixStack.pop();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    protected void renderBoat(T entity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        IBakedModel model = this.cachedBakedModel.getValue();
        if (model != null) {
            matrixStack.push();
            matrixStack.scale(scale, scale, scale);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
            RenderHelper.disableStandardItemLighting();
            Minecraft.getInstance().getItemRenderer()
                    .renderModel(model, ItemStack.EMPTY, packedLight, OverlayTexture.NO_OVERLAY, matrixStack,
                            buffer.getBuffer(RenderType.getTranslucent()));
            RenderHelper.enableStandardItemLighting();
            if (!entity.canSwim()) {
                IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getWaterMask());
                matrixStack.scale(0.75F, 0, 2.8F);
                matrixStack.translate(0, 0, -0.05F);
                BOAT_MODEL.func_228245_c_().render(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY);
            }
            matrixStack.pop();

        }
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull T entity) {
        return textureLocation;
    }
}

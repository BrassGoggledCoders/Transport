package xyz.brassgoggledcoders.transport.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import xyz.brassgoggledcoders.transport.entity.CargoCarrierMinecartEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class CargoCarrierMinecartEntityRenderer extends MinecartRenderer<CargoCarrierMinecartEntity> {
    public CargoCarrierMinecartEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(CargoCarrierMinecartEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer buffer, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLightIn);
        matrixStack.push();
        long i = (long)entity.getEntityId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f = (((float)(i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f1 = (((float)(i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f2 = (((float)(i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStack.translate(f, f1, f2);
        double d0 = MathHelper.lerp(partialTicks, entity.lastTickPosX, entity.getPosX());
        double d1 = MathHelper.lerp(partialTicks, entity.lastTickPosY, entity.getPosY());
        double d2 = MathHelper.lerp(partialTicks, entity.lastTickPosZ, entity.getPosZ());
        Vec3d vec3d = entity.getPos(d0, d1, d2);
        float f3 = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
        if (vec3d != null) {
            Vec3d vec3d1 = entity.getPosOffset(d0, d1, d2, 0.3F);
            Vec3d vec3d2 = entity.getPosOffset(d0, d1, d2, -0.3F);
            if (vec3d1 == null) {
                vec3d1 = vec3d;
            }

            if (vec3d2 == null) {
                vec3d2 = vec3d;
            }

            matrixStack.translate(vec3d.x - d0, (vec3d1.y + vec3d2.y) / 2.0D - d1, vec3d.z - d2);
            Vec3d vec3d3 = vec3d2.add(-vec3d1.x, -vec3d1.y, -vec3d1.z);
            if (vec3d3.length() != 0.0D) {
                vec3d3 = vec3d3.normalize();
                entityYaw = (float)(Math.atan2(vec3d3.z, vec3d3.x) * 180.0D / Math.PI);
                f3 = (float)(Math.atan(vec3d3.y) * 73.0D);
            }
        }

        matrixStack.translate(0.0D, 0.375D, 0.0D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(-f3));
        float f5 = (float)entity.getRollingAmplitude() - partialTicks;
        float f6 = entity.getDamage() - partialTicks;
        if (f6 < 0.0F) {
            f6 = 0.0F;
        }

        if (f5 > 0.0F) {
            matrixStack.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(f5) * f5 * f6 / 10.0F * (float)entity.getRollingDirection()));
        }

        int j = entity.getDisplayTileOffset();
        BlockState blockstate = entity.getDisplayTile();
        if (blockstate.getRenderType() != BlockRenderType.INVISIBLE) {
            matrixStack.push();
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            matrixStack.translate(-0.5D, (double)((float)(j - 8) / 16.0F), 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
            this.renderBlockState(entity, partialTicks, blockstate, matrixStack, buffer, packedLightIn);
            matrixStack.pop();
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.modelMinecart.setRotationAngles(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.modelMinecart.getRenderType(this.getEntityTexture(entity)));
        this.modelMinecart.render(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }
}

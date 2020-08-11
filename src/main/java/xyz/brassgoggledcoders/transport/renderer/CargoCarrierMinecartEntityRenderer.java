package xyz.brassgoggledcoders.transport.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import xyz.brassgoggledcoders.transport.api.TransportClientAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.renderer.IModuleRenderer;
import xyz.brassgoggledcoders.transport.content.TransportModuleSlots;
import xyz.brassgoggledcoders.transport.entity.CargoCarrierMinecartEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class CargoCarrierMinecartEntityRenderer extends MinecartRenderer<CargoCarrierMinecartEntity> {
    public CargoCarrierMinecartEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(CargoCarrierMinecartEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.push();
        long i = (long) entity.getEntityId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStack.translate(f, f1, f2);
        double d0 = MathHelper.lerp(partialTicks, entity.lastTickPosX, entity.getPosX());
        double d1 = MathHelper.lerp(partialTicks, entity.lastTickPosY, entity.getPosY());
        double d2 = MathHelper.lerp(partialTicks, entity.lastTickPosZ, entity.getPosZ());
        Vector3d vec3d = entity.getPos(d0, d1, d2);
        float pitch = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
        if (vec3d != null) {
            Vector3d vec3d1 = entity.getPosOffset(d0, d1, d2, 0.3F);
            Vector3d vec3d2 = entity.getPosOffset(d0, d1, d2, -0.3F);
            if (vec3d1 == null) {
                vec3d1 = vec3d;
            }

            if (vec3d2 == null) {
                vec3d2 = vec3d;
            }

            matrixStack.translate(vec3d.x - d0, (vec3d1.y + vec3d2.y) / 2.0D - d1, vec3d.z - d2);
            Vector3d vec3d3 = vec3d2.add(-vec3d1.x, -vec3d1.y, -vec3d1.z);
            if (vec3d3.length() != 0.0D) {
                vec3d3 = vec3d3.normalize();
                entityYaw = (float) (Math.atan2(vec3d3.z, vec3d3.x) / Math.PI) * 180F;
                pitch = (float) (Math.atan(vec3d3.y) * 73.0D);
            }
        }

        entityYaw = entityYaw % 360;
        if (entityYaw < 0) {
            entityYaw += 360;
        }
        entityYaw += 360;

        double rotationYaw = (entity.rotationYaw + 180) % 360;
        if (rotationYaw < 0) {
            rotationYaw = rotationYaw + 360;
        }
        rotationYaw = rotationYaw + 360;

        if (Math.abs(entityYaw - rotationYaw) > 90) {
            entityYaw += 180;
            pitch = -pitch;
        }

        matrixStack.translate(0.0D, 0.375D, 0.0D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(-pitch));
        float f5 = (float) entity.getRollingAmplitude() - partialTicks;
        float f6 = entity.getDamage() - partialTicks;
        if (f6 < 0.0F) {
            f6 = 0.0F;
        }

        if (f5 > 0.0F) {
            matrixStack.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(f5) * f5 * f6 / 10.0F * (float) entity.getRollingDirection()));
        }

        this.renderModules(entity.getModularEntity(), entityYaw, partialTicks, matrixStack, buffer, packedLight);

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.modelMinecart.setRotationAngles(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.modelMinecart.getRenderType(this.getEntityTexture(entity)));
        this.modelMinecart.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }

    private void renderModules(IModularEntity modularEntity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                               IRenderTypeBuffer buffer, int packedLight) {
        ModuleInstance<?> cargoSlotModuleInstance = modularEntity.getModuleInstance(TransportModuleSlots.CARGO.get());
        if (cargoSlotModuleInstance != null) {
            IModuleRenderer moduleRenderer = TransportClientAPI.getModuleRenderer(cargoSlotModuleInstance.getModule());
            if (moduleRenderer != null) {
                matrixStack.push();
                moduleRenderer.render(cargoSlotModuleInstance, entityYaw, partialTicks, matrixStack, buffer, packedLight);
                matrixStack.pop();
            }
        }

        ModuleInstance<?> backSlotModuleInstance = modularEntity.getModuleInstance(TransportModuleSlots.BACK.get());
        if (backSlotModuleInstance != null) {
            IModuleRenderer moduleRenderer = TransportClientAPI.getModuleRenderer(backSlotModuleInstance.getModule());
            if (moduleRenderer != null) {
                matrixStack.push();
                matrixStack.translate(0.65F, 0F, -0.125F);
                matrixStack.rotate(new Quaternion(90, 90, 0, true));
                moduleRenderer.render(backSlotModuleInstance, entityYaw, partialTicks, matrixStack, buffer, packedLight);
                matrixStack.pop();
            }
        }
    }
}

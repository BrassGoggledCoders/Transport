package xyz.brassgoggledcoders.transport.renderer.boat;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import xyz.brassgoggledcoders.transport.api.TransportClientAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.renderer.IModuleRenderer;
import xyz.brassgoggledcoders.transport.content.TransportModuleSlots;
import xyz.brassgoggledcoders.transport.entity.ModularBoatEntity;

import javax.annotation.Nonnull;

public class ModularBoatRenderer extends EntityRenderer<ModularBoatEntity> {
    private static final ResourceLocation DEFAULT_BOAT = new ResourceLocation("textures/entity/boat/oak.png");
    private final PaddlelessBoatModel boatModel = new PaddlelessBoatModel();

    public ModularBoatRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.8F;
    }

    @Override
    public void render(ModularBoatEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       @Nonnull IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.push();
        matrixStack.translate(0.0D, 0.375D, 0.0D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        float f = (float)entity.getTimeSinceHit() - partialTicks;
        float f1 = entity.getDamageTaken() - partialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            matrixStack.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(f) * f * f1 / 10.0F * (float)entity.getForwardDirection()));
        }

        float f2 = entity.getRockingAngle(partialTicks);
        if (!MathHelper.epsilonEquals(f2, 0.0F)) {
            matrixStack.rotate(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), entity.getRockingAngle(partialTicks), true));
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));

        this.renderModules(entity.getModularEntity(), entityYaw, partialTicks, matrixStack, buffer, packedLight);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.boatModel.getRenderType(this.getEntityTexture(entity)));
        this.boatModel.setRotationAngles(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        this.boatModel.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!entity.canSwim()) {
            IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getWaterMask());
            this.boatModel.func_228245_c_().render(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY);
        }

        matrixStack.pop();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(ModularBoatEntity entity) {
        ResourceLocation textureLocation = entity.getHullType().getEntityTexture(entity);
        return textureLocation != null ? textureLocation : DEFAULT_BOAT;
    }

    private void renderModules(IModularEntity modularEntity, float entityYaw, float partialTicks, MatrixStack matrixStack,
                               IRenderTypeBuffer buffer, int packedLight) {
        ModuleInstance<?> cargoSlotModuleInstance = modularEntity.getModuleInstance(TransportModuleSlots.CARGO.get());
        if (cargoSlotModuleInstance != null) {
            IModuleRenderer moduleRenderer = TransportClientAPI.getModuleRenderer(cargoSlotModuleInstance.getModule());
            if (moduleRenderer != null) {
                matrixStack.push();
                matrixStack.translate(0F, -0.5F, 0F);
                matrixStack.scale(1.2F, 1.2F, 1.2F);
                moduleRenderer.render(cargoSlotModuleInstance, entityYaw, partialTicks, matrixStack, buffer, packedLight);
                matrixStack.pop();
            }
        }

        ModuleInstance<?> backSlotModuleInstance = modularEntity.getModuleInstance(TransportModuleSlots.BACK.get());
        if (backSlotModuleInstance != null) {
            IModuleRenderer moduleRenderer = TransportClientAPI.getModuleRenderer(backSlotModuleInstance.getModule());
            if (moduleRenderer != null) {
                matrixStack.push();
                matrixStack.translate(1.015F, 0F, -0.125F);
                matrixStack.rotate(new Quaternion(90, 90, 0, true));
                moduleRenderer.render(backSlotModuleInstance, entityYaw, partialTicks, matrixStack, buffer, packedLight);
                matrixStack.pop();
            }
        }
    }
}

package xyz.brassgoggledcoders.transport.renderer.boat;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

public class ModularBoatRenderer extends HulledBoatRender<ModularBoatEntity> {

    public ModularBoatRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected void renderAdditions(ModularBoatEntity entity, float entityYaw, float partialTicks,
                                   MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        super.renderAdditions(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        this.renderModules(entity.getModularEntity(), entityYaw, partialTicks, matrixStack, buffer, packedLight);
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
                matrixStack.translate(-1.015F, 0F, -0.125F);
                matrixStack.rotate(new Quaternion(90, 270, 0, true));
                moduleRenderer.render(backSlotModuleInstance, entityYaw, partialTicks, matrixStack, buffer, packedLight);
                matrixStack.pop();
            }
        }
    }
}

package xyz.brassgoggledcoders.transport.compat.vanilla.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.spawner.AbstractSpawner;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.renderer.CargoModuleRender;
import xyz.brassgoggledcoders.transport.compat.vanilla.module.cargo.SpawnerCargoModuleInstance;

public class SpawnerCargoModuleRenderer extends CargoModuleRender {
    @Override
    public void render(ModuleInstance<?> moduleInstance, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        super.render(moduleInstance, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        if (moduleInstance instanceof SpawnerCargoModuleInstance) {
            matrixStack.push();
            matrixStack.translate(0.5D, 0.0D, 0.5D);
            AbstractSpawner spawner = ((SpawnerCargoModuleInstance) moduleInstance).getSpawner();
            Entity entity = spawner.getCachedEntity();
            if (entity != null) {
                float f = 0.53125F;
                float f1 = Math.max(entity.getWidth(), entity.getHeight());
                if ((double) f1 > 1.0D) {
                    f /= f1;
                }

                matrixStack.translate(0.0D, 0.4F, 0.0D);
                matrixStack.rotate(Vector3f.YP.rotationDegrees((float) MathHelper.lerp(partialTicks,
                        spawner.getPrevMobRotation(), spawner.getMobRotation()) * 10.0F));
                matrixStack.translate(0.0D, -0.2F, 0.0D);
                matrixStack.rotate(Vector3f.XP.rotationDegrees(-30.0F));
                matrixStack.scale(f, f, f);
                Minecraft.getInstance().getRenderManager().renderEntityStatic(entity, 0.0D, 0.0D, 0.0D,
                        0.0F, partialTicks, matrixStack, buffer, packedLight);
            }

            matrixStack.pop();
        }
    }
}

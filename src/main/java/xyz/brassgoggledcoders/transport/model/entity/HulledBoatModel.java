package xyz.brassgoggledcoders.transport.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Predicate;

public class HulledBoatModel<T extends BoatEntity> extends SegmentedModel<T> {
    private final ModelRenderer[] paddles = new ModelRenderer[2];
    private final ModelRenderer noWater;
    private final ImmutableList<ModelRenderer> field_228243_f_;

    private final Predicate<T> renderPaddles;

    public HulledBoatModel(Predicate<T> renderPaddles) {
        this.renderPaddles = renderPaddles;
        ModelRenderer[] modelRenderers = new ModelRenderer[]{
                (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64),
                (new ModelRenderer(this, 0, 19)).setTextureSize(128, 64),
                (new ModelRenderer(this, 0, 27)).setTextureSize(128, 64),
                (new ModelRenderer(this, 0, 35)).setTextureSize(128, 64),
                (new ModelRenderer(this, 0, 43)).setTextureSize(128, 64)
        };

        modelRenderers[0].addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 3.0F, 0.0F);
        modelRenderers[0].setRotationPoint(0.0F, 3.0F, 1.0F);
        modelRenderers[1].addBox(-13.0F, -7.0F, -1.0F, 18.0F, 6.0F, 2.0F, 0.0F);
        modelRenderers[1].setRotationPoint(-15.0F, 4.0F, 4.0F);
        modelRenderers[2].addBox(-8.0F, -7.0F, -1.0F, 16.0F, 6.0F, 2.0F, 0.0F);
        modelRenderers[2].setRotationPoint(15.0F, 4.0F, 0.0F);
        modelRenderers[3].addBox(-14.0F, -7.0F, -1.0F, 28.0F, 6.0F, 2.0F, 0.0F);
        modelRenderers[3].setRotationPoint(0.0F, 4.0F, -9.0F);
        modelRenderers[4].addBox(-14.0F, -7.0F, -1.0F, 28.0F, 6.0F, 2.0F, 0.0F);
        modelRenderers[4].setRotationPoint(0.0F, 4.0F, 9.0F);
        modelRenderers[0].rotateAngleX = ((float) Math.PI / 2F);
        modelRenderers[1].rotateAngleY = ((float) Math.PI * 1.5F);
        modelRenderers[2].rotateAngleY = ((float) Math.PI / 2F);
        modelRenderers[3].rotateAngleY = (float) Math.PI;
        this.paddles[0] = this.makePaddle(true);
        this.paddles[0].setRotationPoint(3.0F, -5.0F, 9.0F);
        this.paddles[1] = this.makePaddle(false);
        this.paddles[1].setRotationPoint(3.0F, -5.0F, -9.0F);
        this.paddles[1].rotateAngleY = (float) Math.PI;
        this.paddles[0].rotateAngleZ = 0.19634955F;
        this.paddles[1].rotateAngleZ = 0.19634955F;
        this.noWater = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
        this.noWater.addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 3.0F, 0.0F);
        this.noWater.setRotationPoint(0.0F, -3.0F, 1.0F);
        this.noWater.rotateAngleX = ((float) Math.PI / 2F);
        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.addAll(Arrays.asList(modelRenderers));
        builder.addAll(Arrays.asList(this.paddles));
        this.field_228243_f_ = builder.build();
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setRotationAngles(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch) {
        this.rotatePaddles(entity, 0, limbSwing);
        this.rotatePaddles(entity, 1, limbSwing);
    }

    @Nonnull
    @Override
    public ImmutableList<ModelRenderer> getParts() {
        return this.field_228243_f_;
    }

    public ModelRenderer func_228245_c_() {
        return this.noWater;
    }

    protected ModelRenderer makePaddle(boolean p_187056_1_) {
        ModelRenderer modelrenderer = (new ModelRenderer(this, 62, p_187056_1_ ? 0 : 20)).setTextureSize(128, 64);
        modelrenderer.addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F);
        modelrenderer.addBox(p_187056_1_ ? -1.001F : 0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F);
        return modelrenderer;
    }

    protected void rotatePaddles(T boatEntity, int paddle, float limbSwing) {
        if (renderPaddles.test(boatEntity)) {
            float f = boatEntity.getRowingTime(paddle, limbSwing);
            ModelRenderer modelrenderer = this.paddles[paddle];
            modelrenderer.showModel = true;
            modelrenderer.rotateAngleX = (float) MathHelper.clampedLerp(-(float) Math.PI / 3F, -0.2617994F, (MathHelper.sin(-f) + 1.0F) / 2.0F);
            modelrenderer.rotateAngleY = (float) MathHelper.clampedLerp(-(float) Math.PI / 4F, (float) Math.PI / 4F, (MathHelper.sin(-f + 1.0F) + 1.0F) / 2.0F);
            if (paddle == 1) {
                modelrenderer.rotateAngleY = (float) Math.PI - modelrenderer.rotateAngleY;
            }
        } else {
            ModelRenderer modelrenderer = this.paddles[paddle];
            modelrenderer.showModel = false;
        }
    }

}

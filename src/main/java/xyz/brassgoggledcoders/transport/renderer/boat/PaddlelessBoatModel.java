package xyz.brassgoggledcoders.transport.renderer.boat;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import xyz.brassgoggledcoders.transport.entity.ModularBoatEntity;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class PaddlelessBoatModel extends SegmentedModel<ModularBoatEntity> {
    private final ModelRenderer noWater;
    private final ImmutableList<ModelRenderer> parts;

    public PaddlelessBoatModel() {
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
        modelRenderers[0].rotateAngleX = ((float)Math.PI / 2F);
        modelRenderers[1].rotateAngleY = ((float)Math.PI * 1.5F);
        modelRenderers[2].rotateAngleY = ((float)Math.PI / 2F);
        modelRenderers[3].rotateAngleY = (float)Math.PI;
        this.noWater = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
        this.noWater.addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 3.0F, 0.0F);
        this.noWater.setRotationPoint(0.0F, -3.0F, 1.0F);
        this.noWater.rotateAngleX = ((float)Math.PI / 2F);
        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.addAll(Arrays.asList(modelRenderers));
        this.parts = builder.build();
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setRotationAngles(@Nonnull ModularBoatEntity entity, float limbSwing, float limbSwingAmount,
                                  float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Nonnull
    public ImmutableList<ModelRenderer> getParts() {
        return this.parts;
    }

    public ModelRenderer func_228245_c_() {
        return this.noWater;
    }

}

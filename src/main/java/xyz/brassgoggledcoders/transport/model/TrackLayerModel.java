package xyz.brassgoggledcoders.transport.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;

/**
 * ModelTrackLayer - Either Mojang or a mod author
 * Created using Tabula 6.0.0
 */
public class TrackLayerModel<T extends Entity> extends SegmentedModel<T> {
    public ModelRenderer CartBase;
    public ModelRenderer Piston;
    public ModelRenderer LeftBarTop;
    public ModelRenderer RightBarBottom;
    public ModelRenderer Chest;
    public ModelRenderer FrontLeftBrace;
    public ModelRenderer BackLeftBrace;
    public ModelRenderer FrontRightBrace;
    public ModelRenderer BackRightBrace;
    public ModelRenderer WheelBackLeft;
    public ModelRenderer WheelFrontLeft;
    public ModelRenderer WheelBackRight;
    public ModelRenderer WheelFrontRight;
    public ModelRenderer TopBrace2;
    public ModelRenderer TopBrace1;
    public ModelRenderer Hopper1;
    public ModelRenderer RightBarTop;
    public ModelRenderer LeftBarTop_1;
    public ModelRenderer PistonHead;
    public ModelRenderer PistonHead_1;
    public ModelRenderer ChestLid;
    public ModelRenderer Latch;
    public ModelRenderer Hopper2;
    public ModelRenderer Hopper3;

    public TrackLayerModel() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.Chest = new ModelRenderer(this, 64, 29);
        this.Chest.setRotationPoint(-7.5F, -2.0F, 0.0F);
        this.Chest.addBox(-7.0F, -5.0F, -14.0F, 14, 10, 14, 0.0F);
        this.setRotateAngle(Chest, 0.0F, -1.5707963267948966F, 0.0F);
        this.RightBarTop = new ModelRenderer(this, 0, 29);
        this.RightBarTop.setRotationPoint(-10.0F, -7.0F, -9.0F);
        this.RightBarTop.addBox(0.0F, 0.0F, -1.0F, 37, 2, 2, 0.0F);
        this.Hopper2 = new ModelRenderer(this, 68, 53);
        this.Hopper2.setRotationPoint(-8.0F, -3.0F, -7.0F);
        this.Hopper2.addBox(-5.0F, -7.0F, -12.0F, 9, 3, 9, 0.0F);
        this.setRotateAngle(Hopper2, 0.0F, -1.5707963267948966F, 0.0F);
        this.FrontLeftBrace = new ModelRenderer(this, 0, 35);
        this.FrontLeftBrace.setRotationPoint(27.0F, -19.0F, 10.0F);
        this.FrontLeftBrace.addBox(-1.0F, -1.0F, -1.0F, 2, 24, 2, 0.0F);
        this.WheelFrontLeft = new ModelRenderer(this, 50, 0);
        this.WheelFrontLeft.setRotationPoint(27.0F, 3.0F, 12.0F);
        this.WheelFrontLeft.addBox(-2.0F, -2.0F, -1.0F, 4, 4, 2, 0.0F);
        this.WheelBackRight = new ModelRenderer(this, 50, 0);
        this.WheelBackRight.setRotationPoint(9.0F, 3.0F, -12.0F);
        this.WheelBackRight.addBox(-2.0F, -2.0F, -1.0F, 4, 4, 2, 0.0F);
        this.CartBase = new ModelRenderer(this, 0, 0);
        this.CartBase.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.CartBase.addBox(-10.0F, -8.0F, -1.0F, 20, 16, 10, 0.0F);
        this.setRotateAngle(CartBase, 1.5707963267948966F, 0.0F, 0.0F);
        this.LeftBarTop_1 = new ModelRenderer(this, 0, 29);
        this.LeftBarTop_1.setRotationPoint(-10.0F, -3.0F, 8.0F);
        this.LeftBarTop_1.addBox(0.0F, 0.0F, 0.0F, 37, 2, 2, 0.0F);
        this.ChestLid = new ModelRenderer(this, 64, 83);
        this.ChestLid.setRotationPoint(-7.0F, -5.0F, 0.0F);
        this.ChestLid.addBox(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
        this.setRotateAngle(ChestLid, -0.8651597102135892F, 0.0F, 0.0F);
        this.PistonHead = new ModelRenderer(this, 0, 83);
        this.PistonHead.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.PistonHead.addBox(-8.0F, 4.0F, -8.0F, 16, 4, 16, 0.0F);
        this.LeftBarTop = new ModelRenderer(this, 0, 29);
        this.LeftBarTop.setRotationPoint(-10.0F, -7.0F, 8.0F);
        this.LeftBarTop.addBox(0.0F, 0.0F, 0.0F, 37, 2, 2, 0.0F);
        this.Latch = new ModelRenderer(this, 0, 0);
        this.Latch.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Latch.addBox(6.0F, -3.0F, -15.0F, 2, 4, 1, 0.0F);
        this.WheelFrontRight = new ModelRenderer(this, 50, 0);
        this.WheelFrontRight.setRotationPoint(27.0F, 3.0F, -12.0F);
        this.WheelFrontRight.addBox(-2.0F, -2.0F, -1.0F, 4, 4, 2, 0.0F);
        this.Hopper1 = new ModelRenderer(this, 68, 53);
        this.Hopper1.setRotationPoint(-8.0F, -2.0F, 0.5F);
        this.Hopper1.addBox(-7.0F, -7.0F, -14.0F, 13, 3, 13, 0.0F);
        this.setRotateAngle(Hopper1, 0.0F, -1.5707963267948966F, 0.0F);
        this.FrontRightBrace = new ModelRenderer(this, 0, 35);
        this.FrontRightBrace.setRotationPoint(27.0F, -19.0F, -10.0F);
        this.FrontRightBrace.addBox(-1.0F, -1.0F, -1.0F, 2, 24, 2, 0.0F);
        this.TopBrace1 = new ModelRenderer(this, 4, 37);
        this.TopBrace1.setRotationPoint(18.0F, -20.01F, 0.0F);
        this.TopBrace1.addBox(-1.0F, -1.0F, -14.0F, 2, 2, 28, 0.0F);
        this.setRotateAngle(TopBrace1, 0.0F, 0.7330382858376184F, 0.0F);
        this.RightBarBottom = new ModelRenderer(this, 0, 29);
        this.RightBarBottom.setRotationPoint(-10.0F, -3.0F, -9.0F);
        this.RightBarBottom.addBox(0.0F, 0.0F, -1.0F, 37, 2, 2, 0.0F);
        this.BackRightBrace = new ModelRenderer(this, 0, 35);
        this.BackRightBrace.setRotationPoint(10.0F, -19.0F, -10.0F);
        this.BackRightBrace.addBox(-2.0F, -1.0F, -1.0F, 2, 24, 2, 0.0F);
        this.Hopper3 = new ModelRenderer(this, 68, 52);
        this.Hopper3.setRotationPoint(-4.9F, -1.5F, -7.0F);
        this.Hopper3.addBox(-2.5F, -7.0F, -9.5F, 4, 13, 4, 0.0F);
        this.setRotateAngle(Hopper3, -1.5707963267948966F, -1.5707963267948966F, 0.0F);
        this.WheelBackLeft = new ModelRenderer(this, 50, 0);
        this.WheelBackLeft.setRotationPoint(9.0F, 3.0F, 12.0F);
        this.WheelBackLeft.addBox(-2.0F, -2.0F, -1.0F, 4, 4, 2, 0.0F);
        this.BackLeftBrace = new ModelRenderer(this, 0, 35);
        this.BackLeftBrace.setRotationPoint(10.0F, -19.0F, 10.0F);
        this.BackLeftBrace.addBox(-2.0F, -1.0F, -1.0F, 2, 24, 2, 0.0F);
        this.Piston = new ModelRenderer(this, 60, 0);
        this.Piston.setRotationPoint(18.0F, -12.0F, 0.0F);
        this.Piston.addBox(-8.0F, -8.0F, -8.0F, 16, 12, 16, 0.0F);
        this.TopBrace2 = new ModelRenderer(this, 4, 37);
        this.TopBrace2.setRotationPoint(18.0F, -20.0F, 0.0F);
        this.TopBrace2.addBox(-1.0F, -1.0F, -14.0F, 2, 2, 28, 0.0F);
        this.setRotateAngle(TopBrace2, 0.0F, -0.7330382858376184F, 0.0F);
        this.PistonHead_1 = new ModelRenderer(this, 36, 37);
        this.PistonHead_1.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.PistonHead_1.addBox(-3.0F, 1.0F, -3.0F, 6, 3, 6, 0.0F);
        this.Hopper1.addChild(this.Hopper2);
        this.Chest.addChild(this.ChestLid);
        this.Piston.addChild(this.PistonHead);
        this.ChestLid.addChild(this.Latch);
        this.Hopper2.addChild(this.Hopper3);
        this.Piston.addChild(this.PistonHead_1);
    }

    @Override
    public void setRotationAngles(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch) {

    }

    @Override
    @Nonnull
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
                CartBase,
                Piston,
                LeftBarTop,
                RightBarBottom,
                Chest,
                FrontLeftBrace,
                BackLeftBrace,
                FrontRightBrace,
                BackRightBrace,
                WheelBackLeft,
                WheelFrontLeft,
                WheelBackRight,
                WheelFrontRight,
                TopBrace2,
                TopBrace1,
                Hopper1,
                RightBarTop,
                LeftBarTop_1,
                PistonHead,
                PistonHead_1,
                ChestLid,
                Latch,
                Hopper2,
                Hopper3
        );
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}

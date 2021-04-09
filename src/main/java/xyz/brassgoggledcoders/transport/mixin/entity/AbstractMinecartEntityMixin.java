package xyz.brassgoggledcoders.transport.mixin.entity;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.brassgoggledcoders.transport.util.MinecartMotionHelper;

@Mixin({
        AbstractMinecartEntity.class
})
public class AbstractMinecartEntityMixin {


    @Inject(
            method = {
                    "tick"
            },
            at = {
                    @At("HEAD")
            },
            cancellable = true
    )
    @SuppressWarnings("ConstantConditions")
    private void doMotion(CallbackInfo callbackInfo) {
        MinecartMotionHelper.doMotion((AbstractMinecartEntity) (Object) this);
        callbackInfo.cancel();
    }
}

package net.thegrimsey.transportables.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.thegrimsey.transportables.blocks.IronLadder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
*   Mixin for climb speed on IronLadders.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityClimbSpeedMixin {
    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At("TAIL"), method = "method_26318", cancellable = true)
    private void method_26318(Vec3d vec3d, float f, CallbackInfoReturnable<Vec3d> infoReturnable) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        if ((livingEntity.horizontalCollision) && livingEntity.isClimbing() && livingEntity.getClimbingPos().isPresent()) {
            if(livingEntity.getEntityWorld().getBlockState(livingEntity.getClimbingPos().get()).getBlock() instanceof IronLadder){
                // Set vertical velocity equal to climbSpeed.
                Vec3d velocity = new Vec3d(livingEntity.getVelocity().x, IronLadder.climbSpeed, livingEntity.getVelocity().z);
                // Override velocity return.
                infoReturnable.setReturnValue(velocity);
            }
        }
    }
}

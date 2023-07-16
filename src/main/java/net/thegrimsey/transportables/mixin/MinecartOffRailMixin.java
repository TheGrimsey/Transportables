package net.thegrimsey.transportables.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.thegrimsey.transportables.blocks.Launching_Rail;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class MinecartOffRailMixin {
    /*
    *   Usually Minecart horizontal speed is clamped by off rail speed. That messes things up for launching rails.
    *   Why? LAUNCH VELOCITY. Increasing it means we can do speedy things and get better launch movement.
     */
    @ModifyVariable(method = "moveOffRail()V", at = @At(value = "STORE"), ordinal = 0)
    private double d(double d) {
        return 1000.0;
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method="moveOffRail()V", at = @At(value = "INVOKE"), cancellable = true)
    public void moveOffRail(CallbackInfo ci) {
        AbstractMinecartEntity minecart = (AbstractMinecartEntity) (Object) this;
        BlockState state = minecart.getWorld().getBlockState(minecart.getBlockPos());

        // LAUNCHING RAIl.
        if (state.getBlock() instanceof Launching_Rail) {
            // Apply vertical velocity.
            minecart.setVelocity(minecart.getVelocity().x, Launching_Rail.VERTICAL_VELOCITY, minecart.getVelocity().z);
            minecart.move(MovementType.SELF, minecart.getVelocity());
            ci.cancel();
        }
    }
}

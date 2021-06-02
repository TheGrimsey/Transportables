package net.thegrimsey.transportables.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.thegrimsey.transportables.blocks.AbstractActionRail;
import net.thegrimsey.transportables.blocks.Launching_Rail;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class MinecartRailBoostMixin {

    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At("HEAD"), method = "moveOnRail", cancellable = true)
    protected void moveOnRail(BlockPos pos, BlockState state, CallbackInfo info) {
        // Forward rail moving to rail block. This makes it much easier to handle with just overriding the functions in subclasses.
        AbstractRailBlock railBlock = (AbstractRailBlock) state.getBlock();
        if (railBlock instanceof AbstractActionRail) {
            ((AbstractActionRail) railBlock).onMoveOnRail(pos, state, (AbstractMinecartEntity) (Object) this, info);
        }
    }

    /*
     *	Completely overwriting it may be bad BUT
     * 	I motivate doing this by saying that I searched Github and no open source repo there modified this function. I can't provide compatibility with closed source mods.
     *	The changes I made here allow for a much nicer launch.
     */
    @SuppressWarnings("ConstantConditions")
    @Overwrite
    public void moveOffRail() {
        AbstractMinecartEntity minecart = (AbstractMinecartEntity) (Object) this;
        BlockState state = minecart.world.getBlockState(minecart.getBlockPos());

        // LAUNCHING RAIl.
        if (state.getBlock() instanceof Launching_Rail) {
            // Apply vertical velocity.
            minecart.setVelocity(minecart.getVelocity().x, Launching_Rail.VERTICAL_VELOCITY, minecart.getVelocity().z);
        }

        minecart.move(MovementType.SELF, minecart.getVelocity());
        minecart.setVelocity(minecart.getVelocity().multiply(minecart.isOnGround() ? 0.5D : 0.95D));
    }
}

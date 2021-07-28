package net.thegrimsey.transportables.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.thegrimsey.transportables.blocks.AbstractActionRail;
import org.spongepowered.asm.mixin.Mixin;
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
        if (railBlock instanceof AbstractActionRail actionRail) {
            actionRail.onMoveOnRail(pos, state, (AbstractMinecartEntity) (Object) this, info);
        }
    }
}

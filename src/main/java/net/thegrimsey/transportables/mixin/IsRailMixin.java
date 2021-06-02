package net.thegrimsey.transportables.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.thegrimsey.transportables.blocks.Launching_Rail;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractRailBlock.class)
public abstract class IsRailMixin {
    //Override static IsRail function so we can tell the game that we aren't a rail when we want to launch off.
    @Inject(at = @At("HEAD"), method = "isRail(Lnet/minecraft/block/BlockState;)Z", cancellable = true)
    private static void isRail(BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (state.getBlock() instanceof Launching_Rail && state.get(Launching_Rail.POWERED))
            info.setReturnValue(false);
    }
}

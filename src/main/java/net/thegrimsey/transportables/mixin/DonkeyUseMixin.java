package net.thegrimsey.transportables.mixin;

import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.thegrimsey.transportables.items.LinkerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
 *   Unlike horses, donkeys do not call itemStack.useOnEntity when interacted with.
 *   This makes it so the linker doesn't work on donkeys.
 *   This mixin simply puts a check for that at the top of the interactMob function in AbstractDonkeyEntity for when you use the linker.
 *
 *   Should act as Horse does.
 */
@Mixin(AbstractDonkeyEntity.class)
public abstract class DonkeyUseMixin {

    @Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
    private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        AbstractDonkeyEntity entity = (AbstractDonkeyEntity) (Object) this;
        if (!entity.isBaby() && !player.shouldCancelInteraction()) {
            ItemStack handStack = player.getStackInHand(hand);
            if (!handStack.isEmpty() && handStack.getItem() instanceof LinkerItem) {
                ActionResult actionResult = handStack.useOnEntity(player, entity, hand);
                if (actionResult.isAccepted())
                    info.setReturnValue(actionResult);
            }
        }
    }
}

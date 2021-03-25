package net.thegrimsey.transportables.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.thegrimsey.transportables.Transportables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class MinecartRailBoostMixin {


	@Inject(at = @At("HEAD"), method = "moveOnRail")
	protected void moveOnRail(BlockPos pos, BlockState state, CallbackInfo info)
	{
		AbstractMinecartEntity entity = (AbstractMinecartEntity)(Object)this;
		AbstractRailBlock abstractRailBlock = (AbstractRailBlock)state.getBlock();

		if (abstractRailBlock == Transportables.HIGHPOWERED_RAIL) {
			RailShape railShape = (RailShape)state.get(abstractRailBlock.getShapeProperty());
			Vec3d velocity = entity.getVelocity();
			double squaredSpeed = Math.sqrt(entity.squaredHorizontalLength(velocity));

			if (squaredSpeed > 0.01D) {
				double accelerationScale = 0.06D;
				entity.setVelocity(velocity.add(velocity.x / squaredSpeed * accelerationScale, 0.0D, velocity.z / squaredSpeed * accelerationScale));
			} else {
				double x = velocity.x;
				double z = velocity.z;
				if (railShape == RailShape.EAST_WEST) {
					if (((EntityAccessor)entity).invokeWillHitBlockAt(pos.west())) {
						x = 0.02D;
					} else if (((EntityAccessor)entity).invokeWillHitBlockAt(pos.east())) {
						x = -0.02D;
					}
				} else if(railShape == RailShape.NORTH_SOUTH) {
					if (((EntityAccessor)entity).invokeWillHitBlockAt(pos.north())) {
						z = 0.02D;
					} else if (((EntityAccessor)entity).invokeWillHitBlockAt(pos.south())) {
						z = -0.02D;
					}
				}

				entity.setVelocity(x, velocity.y, z);
			}
		}
	}
}

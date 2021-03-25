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
			Vec3d vec3d7 = entity.getVelocity();
			double af = Math.sqrt(entity.squaredHorizontalLength(vec3d7));
			if (af > 0.01D) {
				double accelerationScale = 0.06D;
				entity.setVelocity(vec3d7.add(vec3d7.x / af * accelerationScale, 0.0D, vec3d7.z / af * accelerationScale));
			} else {
				Vec3d vec3d8 = entity.getVelocity();
				double x = vec3d8.x;
				double z = vec3d8.z;
				if (railShape == RailShape.EAST_WEST) {
					if (((EntityAccessor)entity).invokeWillHitBlockAt(pos.west())) {
						x = 0.02D;
					} else if (((EntityAccessor)entity).invokeWillHitBlockAt(pos.east())) {
						x = -0.02D;
					}
				} else {
					if (railShape != RailShape.NORTH_SOUTH) {
						return;
					}

					if (((EntityAccessor)entity).invokeWillHitBlockAt(pos.north())) {
						z = 0.02D;
					} else if (((EntityAccessor)entity).invokeWillHitBlockAt(pos.south())) {
						z = -0.02D;
					}
				}

				entity.setVelocity(x, vec3d8.y, z);
			}
		}
	}
}

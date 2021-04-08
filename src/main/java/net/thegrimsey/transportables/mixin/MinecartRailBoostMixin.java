package net.thegrimsey.transportables.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.thegrimsey.transportables.TransportablesBlocks;
import net.thegrimsey.transportables.blocks.HighPowered_Rail;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class MinecartRailBoostMixin {

	@SuppressWarnings("ConstantConditions")
	@Inject(at = @At("HEAD"), method = "moveOnRail")
	protected void moveOnRail(BlockPos pos, BlockState state, CallbackInfo info)
	{
		AbstractMinecartEntity minecart = (AbstractMinecartEntity)(Object)this;
		AbstractRailBlock railBlock = (AbstractRailBlock)state.getBlock();

		/* HIGHPOWERED_RAIL */
		if (railBlock == TransportablesBlocks.HIGHPOWERED_RAIL) {
			// Slow down if this rail isn't powered.
			if(!state.get(HighPowered_Rail.POWERED))
			{
				// Minecart will lose 75% of velocity for each tick on the rail.
				double slowDownScaler = 0.25D;
				minecart.setVelocity(minecart.getVelocity().multiply(slowDownScaler, 1.0D, slowDownScaler));
				return;
			}

			RailShape railShape = state.get(railBlock.getShapeProperty());
			Vec3d velocity = minecart.getVelocity();
			double speed = Math.sqrt(Entity.squaredHorizontalLength(velocity));

			// If moving: Accelerate to full-speed in direction.
			if (speed > 0.01D) {
				// Terrible hack.
				// Because Minecart forward rotation is not equal to direction of movement we instead try to extrapolate it from the direction we are moving in.
				// This should set us to maximum speed.
				minecart.setVelocity(velocity.normalize().multiply(((EntityAccessor)minecart).invokeGetMaxOffRailSpeed()*10D));
			} else {
				// We aren't moving so let's check if we will hit blocks that will set us moving.
				HandleBlockHit(pos, minecart, railShape, velocity);
			}
		}
	}

	@Overwrite
	public void moveOffRail()
	{
		AbstractMinecartEntity minecart = (AbstractMinecartEntity)(Object)this;
		BlockState state = minecart.world.getBlockState(minecart.getBlockPos());

		if(state.getBlock() == TransportablesBlocks.LAUNCHING_RAIL)
		{
			minecart.setVelocity(minecart.getVelocity().x, 1D, minecart.getVelocity().z);
		}
		minecart.move(MovementType.SELF, minecart.getVelocity());

		minecart.setVelocity(minecart.getVelocity().multiply(minecart.isOnGround() ? 0.5D : 0.95D));
	}

	private void HandleBlockHit(BlockPos pos, AbstractMinecartEntity entity, RailShape railShape, Vec3d velocity) {
		double x = velocity.x;
		double z = velocity.z;
		if (railShape == RailShape.EAST_WEST) {
			if (((EntityAccessor) entity).invokeWillHitBlockAt(pos.west())) {
				x = 0.02D;
			} else if (((EntityAccessor) entity).invokeWillHitBlockAt(pos.east())) {
				x = -0.02D;
			}
		} else if(railShape == RailShape.NORTH_SOUTH) {
			if (((EntityAccessor) entity).invokeWillHitBlockAt(pos.north())) {
				z = 0.02D;
			} else if (((EntityAccessor) entity).invokeWillHitBlockAt(pos.south())) {
				z = -0.02D;
			}
		}

		entity.setVelocity(x, velocity.y, z);
	}
}

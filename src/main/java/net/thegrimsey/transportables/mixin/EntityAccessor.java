package net.thegrimsey.transportables.mixin;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractMinecartEntity.class)
public interface EntityAccessor {

    @Invoker("willHitBlockAt")
    public boolean invokeWillHitBlockAt(BlockPos pos);

    @Invoker("getMaxOffRailSpeed")
    public double invokeGetMaxOffRailSpeed();

}

package net.thegrimsey.transportables.blocks;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.thegrimsey.transportables.blocks.entity.TeleSender_RailEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public class TeleSender_Rail extends AbstractActionRail implements BlockEntityProvider {
    public TeleSender_Rail(Settings settings) {
        super(true, settings);
    }

    @Override
    public void onMoveOnRail(BlockPos pos, BlockState state, AbstractMinecartEntity minecart, CallbackInfo info) {
        TeleSender_RailEntity blockEntity = (TeleSender_RailEntity)minecart.world.getBlockEntity(pos);
        if(blockEntity == null || !blockEntity.hasDestination())
            return;

        minecart.refreshPositionAndAngles(blockEntity.getDestinationX() + 0.5D, blockEntity.getDestinationY(), blockEntity.getDestinationZ() + 0.5D, minecart.yaw, minecart.pitch);

        System.out.println("Teleporting entity to: X: " + blockEntity.getDestinationX() + " Y: " + blockEntity.getDestinationY() + " Z: " + blockEntity.getDestinationZ());
        // TODO Rotate velocity.

        info.cancel();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new TeleSender_RailEntity();
    }
}

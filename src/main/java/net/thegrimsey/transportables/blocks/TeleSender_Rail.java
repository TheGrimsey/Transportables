package net.thegrimsey.transportables.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.thegrimsey.transportables.blocks.entity.TeleSender_RailEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public class TeleSender_Rail extends AbstractActionRail implements BlockEntityProvider {
    public static final BooleanProperty POWERED = Properties.POWERED;

    public TeleSender_Rail(Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
    }

    @Override
    public void onMoveOnRail(BlockPos pos, BlockState state, AbstractMinecartEntity minecart, CallbackInfo info) {
        TeleSender_RailEntity blockEntity = (TeleSender_RailEntity) minecart.getWorld().getBlockEntity(pos);
        if (blockEntity == null || !blockEntity.hasDestination() || !state.get(POWERED))
            return;

        // Move minecart. Not using teleport because of problems with passengers.
        minecart.refreshPositionAndAngles(blockEntity.getDestinationX() + 0.5D, blockEntity.getDestinationY(), blockEntity.getDestinationZ() + 0.5D, minecart.getYaw(), minecart.getPitch());

        info.cancel();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TeleSender_RailEntity(pos, state);
    }

    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        boolean currentPowerState = state.get(POWERED);
        boolean newPowerState = world.isReceivingRedstonePower(pos);
        if (newPowerState != currentPowerState) {
            world.setBlockState(pos, state.with(POWERED, newPowerState), 3);
            world.updateNeighborsAlways(pos.down(), this);
            if (state.get(SHAPE).isAscending()) {
                world.updateNeighborsAlways(pos.up(), this);
            }
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("transportables.telesender_rail.tooltip_01"));
        tooltip.add(Text.translatable("transportables.telesender_rail.tooltip_02"));

        super.appendTooltip(stack, world, tooltip, options);
    }
}

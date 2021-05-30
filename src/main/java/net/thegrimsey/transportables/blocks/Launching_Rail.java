package net.thegrimsey.transportables.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Launching_Rail extends AbstractActionRail {
    public static final BooleanProperty POWERED = Properties.POWERED;

    public static final double VERTICAL_VELOCITY = 1D;

    public Launching_Rail(Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
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
        tooltip.add(new TranslatableText("transportables.launching_rail.tooltip_01"));

        super.appendTooltip(stack, world, tooltip, options);
    }
}

package net.thegrimsey.transportables.blocks;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractActionRail extends AbstractRailBlock {
    public static final Property<RailShape> SHAPE = Properties.STRAIGHT_RAIL_SHAPE;

    protected AbstractActionRail(boolean allowCurves, Settings settings) {
        super(allowCurves, settings);
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    public abstract void onMoveOnRail(BlockPos pos, BlockState state, AbstractMinecartEntity minecart);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SHAPE);
    }
}

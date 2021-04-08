package net.thegrimsey.transportables.blocks;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Launching_Rail extends AbstractRailBlock {
    public static final Property<RailShape> SHAPE;
    public static final BooleanProperty POWERED;

    public Launching_Rail(Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(POWERED, false));
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
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
        builder.add(SHAPE, POWERED);
    }

    static {
        SHAPE = Properties.STRAIGHT_RAIL_SHAPE;
        POWERED = Properties.POWERED;
    }
}

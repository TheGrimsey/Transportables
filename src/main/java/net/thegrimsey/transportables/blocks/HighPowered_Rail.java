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

public class HighPowered_Rail extends AbstractRailBlock {
    public static final int MAX_POWER_SPREAD_DISTANCE = 8;

    public static final Property<RailShape> SHAPE;
    public static final BooleanProperty POWERED;

    public HighPowered_Rail(Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(POWERED, false));
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }
    protected boolean isPoweredByOtherRails(World world, BlockPos pos, BlockState state, boolean boolean4, int distance) {
        if (distance >= MAX_POWER_SPREAD_DISTANCE) {
            return false;
        } else {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            boolean bl = true;
            RailShape railShape = state.get(SHAPE);
            switch(railShape) {
                case NORTH_SOUTH:
                    if (boolean4) {
                        ++z;
                    } else {
                        --z;
                    }
                    break;
                case EAST_WEST:
                    if (boolean4) {
                        --x;
                    } else {
                        ++x;
                    }
                    break;
                case ASCENDING_EAST:
                    if (boolean4) {
                        --x;
                    } else {
                        ++x;
                        ++y;
                        bl = false;
                    }

                    railShape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_WEST:
                    if (boolean4) {
                        --x;
                        ++y;
                        bl = false;
                    } else {
                        ++x;
                    }

                    railShape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_NORTH:
                    if (boolean4) {
                        ++z;
                    } else {
                        --z;
                        ++y;
                        bl = false;
                    }

                    railShape = RailShape.NORTH_SOUTH;
                    break;
                case ASCENDING_SOUTH:
                    if (boolean4) {
                        ++z;
                        ++y;
                        bl = false;
                    } else {
                        --z;
                    }

                    railShape = RailShape.NORTH_SOUTH;
            }

            if (this.isPoweredByOtherRails(world, new BlockPos(x, y, z), boolean4, distance, railShape)) {
                return true;
            } else {
                return bl && this.isPoweredByOtherRails(world, new BlockPos(x, y - 1, z), boolean4, distance, railShape);
            }
        }
    }

    protected boolean isPoweredByOtherRails(World world, BlockPos pos, boolean bl, int distance, RailShape shape) {
        BlockState blockState = world.getBlockState(pos);
        if (!blockState.isOf(this)) {
            return false;
        } else {
            RailShape railShape = blockState.get(SHAPE);
            if (shape == RailShape.EAST_WEST && (railShape == RailShape.NORTH_SOUTH || railShape == RailShape.ASCENDING_NORTH || railShape == RailShape.ASCENDING_SOUTH)) {
                return false;
            } else if (shape == RailShape.NORTH_SOUTH && (railShape == RailShape.EAST_WEST || railShape == RailShape.ASCENDING_EAST || railShape == RailShape.ASCENDING_WEST)) {
                return false;
            } else if (blockState.get(POWERED)) {
                return world.isReceivingRedstonePower(pos) || this.isPoweredByOtherRails(world, pos, blockState, bl, distance + 1);
            } else {
                return false;
            }
        }
    }

    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        boolean currentPowerState = state.get(POWERED);
        boolean newPowerState = world.isReceivingRedstonePower(pos) || this.isPoweredByOtherRails(world, pos, state, true, 0) || this.isPoweredByOtherRails(world, pos, state, false, 0);
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

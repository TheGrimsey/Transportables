package net.thegrimsey.transportables.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.thegrimsey.transportables.mixin.MinecartEntityAccessor;
import net.thegrimsey.transportables.util.MinecartUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public class HighPowered_Rail extends AbstractActionRail {
    public static final int MAX_POWER_SPREAD_DISTANCE = 8;

    public static final BooleanProperty POWERED = Properties.POWERED;

    public HighPowered_Rail(Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(POWERED, false));
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public void onMoveOnRail(BlockPos pos, BlockState state, AbstractMinecartEntity minecart, CallbackInfo info) {
        // Slow down if this rail isn't powered.
        if(!state.get(HighPowered_Rail.POWERED))
        {
            // Minecart will lose 75% of velocity for each tick on the rail.
            double slowDownScaler = 0.25D;
            minecart.setVelocity(minecart.getVelocity().multiply(slowDownScaler, 1.0D, slowDownScaler));
            return;
        }

        RailShape railShape = state.get(getShapeProperty());
        Vec3d velocity = minecart.getVelocity();
        double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

        // If moving: Accelerate to full-speed in direction.
        if (speed > 0.01D) {
            // Because Minecart forward rotation is not equal to direction of movement we instead try to extrapolate it from the direction we are moving in.
            minecart.setVelocity(velocity.normalize().multiply(((MinecartEntityAccessor)minecart).invokeGetMaxOffRailSpeed()*10D));
        } else {
            // We aren't moving so let's check if we will hit blocks that will set us moving.
            MinecartUtil.HandleBlockHit(pos, minecart, railShape);
        }
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
        super.appendProperties(builder);
        builder.add(POWERED);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new TranslatableText("transportables.highpowered_rail.tooltip_01"));

        super.appendTooltip(stack, world, tooltip, options);
    }
}

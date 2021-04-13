package net.thegrimsey.transportables.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.thegrimsey.transportables.Transportables;
import net.thegrimsey.transportables.TransportablesBlocks;
import net.thegrimsey.transportables.blocks.entity.TeleSender_RailEntity;
import net.thegrimsey.transportables.entity.CarriageEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class RailLinker extends Item {
    public RailLinker() {
        super(new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockState hitBlockState = context.getWorld().getBlockState(context.getBlockPos());

        if(Objects.requireNonNull(context.getPlayer()).isSneaking())
        {
            if(hitBlockState.getBlock() == TransportablesBlocks.TELESENDER_RAIL)
            {
                // Get position from NBT data.
                CompoundTag tag = context.getStack().getOrCreateTag();
                int X = tag.getInt("X");
                int Y = tag.getInt("Y");
                int Z = tag.getInt("Z");

                // Limit teleport distance.
                double sqrDist = context.getBlockPos().getSquaredDistance(X,Y,Z, true);
                int maxDistance = Transportables.CONFIG.TELESENDER_RANGE;
                if(sqrDist > maxDistance * maxDistance)
                {
                    long distance = Math.round(MathHelper.sqrt(sqrDist));
                    context.getPlayer().sendMessage(new TranslatableText("transportables.raillinker.outofrange", distance, maxDistance), true);
                    return ActionResult.FAIL;
                }

                // Set rail's destination.
                TeleSender_RailEntity railEntity = (TeleSender_RailEntity) context.getWorld().getBlockEntity(context.getBlockPos());
                if (railEntity != null) {
                    railEntity.setDestination(X, Y, Z);
                    railEntity.markDirty();
                }

                // Send "Updated destination" message.
                context.getPlayer().sendMessage(new TranslatableText("transportables.raillinker.updateddestination", X, Y, Z), true);
                return ActionResult.success(true);
            }
        }
        else
        {
            // Save position of rail.
            if(hitBlockState.getBlock() instanceof AbstractRailBlock)
            {
                CompoundTag tag = context.getStack().getOrCreateTag();
                tag.putInt("X", context.getBlockPos().getX());
                tag.putInt("Y", context.getBlockPos().getY());
                tag.putInt("Z", context.getBlockPos().getZ());
                context.getStack().setTag(tag);

                context.getPlayer().sendMessage(new TranslatableText("transportables.raillinker.savedposition", context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ()), true);
                return ActionResult.success(true);
            }
        }

        return super.useOnBlock(context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        final String carriageKey = "LinkingCarriage";

        if(entity instanceof HorseEntity) {
            CompoundTag tag = stack.getOrCreateTag();
            System.out.println(tag.toString());
            if(tag.contains(carriageKey))
            {
                int carriageId = tag.getInt(carriageKey);

                CarriageEntity carriageEntity = (CarriageEntity) entity.world.getEntityById(carriageId);
                if(carriageEntity != null)
                {
                    carriageEntity.setCarriageHolder((HorseEntity) entity);

                    user.sendMessage(Text.of("Success."), true);
                    return ActionResult.SUCCESS;
                }
                user.sendMessage(Text.of("Fail: Entity Null."), true);
            }
            else
                user.sendMessage(Text.of("Fail: Tag missing."), true);

            return ActionResult.FAIL;
        } else if (entity instanceof CarriageEntity) {
            user.getStackInHand(hand).getOrCreateTag().putInt(carriageKey, entity.getEntityId());
            user.sendMessage(Text.of("Link start: " + entity.getEntityId()), true);
            return ActionResult.SUCCESS;
        }

        return super.useOnEntity(stack, user, entity, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        // Retrieve position for tooltip.
        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains("X"))
        {
            int x = tag.getInt("X");
            int y = tag.getInt("Y");
            int z = tag.getInt("Z");
            tooltip.add(new TranslatableText("transportables.raillinker.tooltip_01", x, y, z));
        }
        else
        {
            tooltip.add(new TranslatableText("transportables.raillinker.tooltip_01_nodestination"));
        }

        tooltip.add(new TranslatableText("transportables.raillinker.tooltip_02"));
        tooltip.add(new TranslatableText("transportables.raillinker.tooltip_03"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}

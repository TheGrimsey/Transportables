package net.thegrimsey.transportables.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.thegrimsey.transportables.Transportables;
import net.thegrimsey.transportables.TransportablesBlocks;
import net.thegrimsey.transportables.blocks.entity.TeleSender_RailEntity;
import net.thegrimsey.transportables.entity.AbstractCarriageEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LinkerItem extends Item {
    final String carriageKey = "LinkingCarriage";

    public LinkerItem() {
        super(new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockState hitBlockState = context.getWorld().getBlockState(context.getBlockPos());

        if (Objects.requireNonNull(context.getPlayer()).isSneaking()) {
            if (hitBlockState.getBlock() == TransportablesBlocks.TELESENDER_RAIL) {
                // Get position from NBT data.
                NbtCompound tag = context.getStack().getOrCreateNbt();
                int X = tag.getInt("X");
                int Y = tag.getInt("Y");
                int Z = tag.getInt("Z");

                // Limit teleport distance.
                double sqrDist = context.getBlockPos().getSquaredDistance(X, Y, Z);
                long maxDistance = Math.min(Transportables.CONFIG.TELESENDER_RANGE, 3_000_000_000L);
                if (maxDistance == -1 || sqrDist > maxDistance * maxDistance) {
                    long distance = Math.round(MathHelper.sqrt((float) sqrDist));
                    context.getPlayer().sendMessage(Text.translatable("transportables.linker.outofrange", distance, maxDistance), true);
                    return ActionResult.FAIL;
                }

                // Set rail's destination.
                TeleSender_RailEntity railEntity = (TeleSender_RailEntity) context.getWorld().getBlockEntity(context.getBlockPos());
                if (railEntity != null) {
                    railEntity.setDestination(X, Y, Z);
                    railEntity.markDirty();
                }

                // Send "Updated destination" message.
                context.getPlayer().sendMessage(Text.translatable("transportables.linker.updateddestination", X, Y, Z), true);
                return ActionResult.success(true);
            }
        } else {
            // Save position of rail.
            if (hitBlockState.getBlock() instanceof AbstractRailBlock) {
                NbtCompound tag = context.getStack().getOrCreateNbt();
                tag.putInt("X", context.getBlockPos().getX());
                tag.putInt("Y", context.getBlockPos().getY());
                tag.putInt("Z", context.getBlockPos().getZ());
                context.getStack().setNbt(tag);

                context.getPlayer().sendMessage(Text.translatable("transportables.linker.savedposition", context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ()), true);
                return ActionResult.success(true);
            }
        }

        return super.useOnBlock(context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity.world.isClient)
            return ActionResult.PASS;

        if (entity instanceof AbstractHorseEntity) {
            NbtCompound tag = stack.getNbt();
            if (tag != null && tag.contains(carriageKey)) {
                UUID carriageId = tag.getUuid(carriageKey);

                Entity targetEntity = ((ServerWorld) entity.world).getEntity(carriageId);
                if (targetEntity instanceof AbstractCarriageEntity targetCarriageEntity) {
                    // Limit link distance.
                    double sqrDist = entity.getPos().squaredDistanceTo(targetCarriageEntity.getPos());
                    long maxDistance = Math.min(Transportables.CONFIG.CARRIAGE_LINK_RANGE, 3_000_000_000L);
                    if (maxDistance == -1 || sqrDist > maxDistance * maxDistance) {
                        long distance = Math.round(MathHelper.sqrt((float) sqrDist));
                        user.sendMessage(Text.translatable("transportables.linker.carriage_outofrange", distance, maxDistance), true);
                        return ActionResult.SUCCESS;
                    }

                    if(!targetCarriageEntity.canLinkWith((AbstractHorseEntity) entity)) {
                        user.sendMessage(Text.translatable("transportables.linker.carriage_cant_link"), true);
                        return ActionResult.FAIL;
                    }

                    targetCarriageEntity.setCarriageHolder((AbstractHorseEntity) entity);
                    user.sendMessage(Text.translatable("transportables.linker.carriage_linked"), true);

                    tag.remove(carriageKey);
                    user.getStackInHand(hand).setNbt(tag);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        } else if (entity instanceof AbstractCarriageEntity carriageEntity) {
            if (!user.isSneaking())
                return ActionResult.PASS;

            if (carriageEntity.HasCarriageHolder()) {
                carriageEntity.removeCarriageHolder();
                user.sendMessage(Text.translatable("transportables.linker.carriage_unlink"), true);
                return ActionResult.SUCCESS;
            }

            user.sendMessage(Text.translatable("transportables.linker.carriage_link_start"), true);
            user.getStackInHand(hand).getOrCreateNbt().putUuid(carriageKey, entity.getUuid());
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        // Retrieve position for tooltip.
        NbtCompound tag = stack.getNbt();
        if (tag != null && tag.contains("X")) {
            int x = tag.getInt("X");
            int y = tag.getInt("Y");
            int z = tag.getInt("Z");
            tooltip.add(Text.translatable("transportables.linker.tooltip_01", x, y, z));
        } else {
            tooltip.add(Text.translatable("transportables.linker.tooltip_01_nodestination"));
        }

        tooltip.add(Text.translatable("transportables.linker.tooltip_02"));
        tooltip.add(Text.translatable("transportables.linker.tooltip_03"));
        tooltip.add(Text.translatable("transportables.linker.tooltip_04"));
        tooltip.add(Text.translatable("transportables.linker.tooltip_05"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}

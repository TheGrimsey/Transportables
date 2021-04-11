package net.thegrimsey.transportables.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.thegrimsey.transportables.TransportablesBlocks;
import net.thegrimsey.transportables.blocks.entity.TeleSender_RailEntity;

public class RailLinker extends Item {
    public RailLinker() {
        super(new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockState hitBlockState = context.getWorld().getBlockState(context.getBlockPos());

        if(context.getPlayer().isSneaking())
        {
            if(hitBlockState.getBlock() == TransportablesBlocks.TELESENDER_RAIL)
            {
                // Get position:
                CompoundTag tag = context.getStack().getOrCreateTag();
                int X = tag.getInt("X");
                int Y = tag.getInt("Y");
                int Z = tag.getInt("Z");

                TeleSender_RailEntity railEntity = (TeleSender_RailEntity) context.getWorld().getBlockEntity(context.getBlockPos());
                railEntity.setDestination(X, Y, Z);
                railEntity.markDirty();

                context.getPlayer().sendMessage(new TranslatableText("transportables.raillinker.updatedposition", X, Y, Z), true);
                return ActionResult.success(true);
            }
        }
        else
        {
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
}

package net.thegrimsey.transportables.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.thegrimsey.transportables.entity.ChestCarriageEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChestCarriageItem  extends Item {
    public ChestCarriageItem() {
        super(new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(!context.getWorld().isClient())
        {
            ChestCarriageEntity carriage = ChestCarriageEntity.create(context.getWorld(), context.getBlockPos().getX() + 0.5D, context.getBlockPos().getY() + 1.25D, context.getBlockPos().getZ() + 0.5D, context.getPlayerYaw());
            if(context.getStack().hasCustomName())
                carriage.setCustomName(context.getStack().getName());

            context.getWorld().spawnEntity(carriage);
            context.getStack().decrement(1);
        }

        return ActionResult.success(true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("transportables.chest_carriage.tooltip_01"));
        tooltip.add(new TranslatableText("transportables.chest_carriage.tooltip_02"));
        tooltip.add(new TranslatableText("transportables.chest_carriage.tooltip_03"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
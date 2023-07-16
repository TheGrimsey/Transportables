package net.thegrimsey.transportables.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.thegrimsey.transportables.entity.CarriageEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CarriageItem extends Item {
    public CarriageItem() {
        super(new FabricItemSettings().maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (!context.getWorld().isClient()) {
            CarriageEntity carriage = CarriageEntity.create(context.getWorld(), context.getBlockPos().getX() + 0.5D, context.getBlockPos().getY() + 1.25D, context.getBlockPos().getZ() + 0.5D, context.getPlayerYaw());
            if (context.getStack().hasCustomName())
                carriage.setCustomName(context.getStack().getName());

            context.getWorld().spawnEntity(carriage);
            context.getStack().decrement(1);
        }

        return ActionResult.success(true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("transportables.carriage.tooltip_01"));
        tooltip.add(Text.translatable("transportables.carriage.tooltip_02"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}

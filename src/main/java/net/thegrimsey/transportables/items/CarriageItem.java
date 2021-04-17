package net.thegrimsey.transportables.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.thegrimsey.transportables.entity.CarriageEntity;

public class CarriageItem extends Item {
    public CarriageItem() {
        super(new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(!context.getWorld().isClient())
        {
            CarriageEntity carriage = CarriageEntity.create(context.getWorld(), context.getBlockPos().getX() + 0.5D, context.getBlockPos().getY() + 1.25D, context.getBlockPos().getZ() + 0.5D);
            if(context.getStack().hasCustomName())
                carriage.setCustomName(context.getStack().getName());

            context.getWorld().spawnEntity(carriage);
            context.getStack().decrement(1);
        }

        return ActionResult.success(true);
    }
}

package net.thegrimsey.transportables.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.thegrimsey.transportables.TransportablesEntities;
import net.thegrimsey.transportables.TransportablesItems;

public class CarriageEntity extends AbstractCarriageEntity {
    public static CarriageEntity create(World world, double x, double y, double z)
    {
        CarriageEntity carriage = new CarriageEntity(TransportablesEntities.CARRIAGE, world);
        carriage.updatePosition(x, y, z);
        carriage.setVelocity(Vec3d.ZERO);
        carriage.prevX = x;
        carriage.prevY = y;
        carriage.prevZ = z;

        return carriage;
    }

    public CarriageEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void drop(DamageSource source) {
        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            ItemStack drops = new ItemStack(TransportablesItems.CARRIAGE_ITEM);
            if(this.hasCustomName())
                drops.setCustomName(this.getCustomName());

            this.dropStack(drops);
        }
    }
}

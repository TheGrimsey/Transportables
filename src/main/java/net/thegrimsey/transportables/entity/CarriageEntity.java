package net.thegrimsey.transportables.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.thegrimsey.transportables.TransportablesEntities;

public class CarriageEntity extends AbstractCarriageEntity {
    public CarriageEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public static CarriageEntity create(World world, double x, double y, double z, float yaw) {
        CarriageEntity carriage = new CarriageEntity(TransportablesEntities.CARRIAGE, world);
        carriage.setPosition(x, y, z);
        carriage.setVelocity(Vec3d.ZERO);
        carriage.prevX = x;
        carriage.prevY = y;
        carriage.prevZ = z;

        carriage.setYaw(yaw);

        return carriage;
    }
}

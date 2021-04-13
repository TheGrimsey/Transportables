package net.thegrimsey.transportables.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Arm;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CarriageEntity extends LivingEntity {
    @Nullable
    HorseEntity carriageHolder = null;

    UUID owner;

    final float FOLLOW_DISTANCE = 3F;

    public CarriageEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public void SetOwner(PlayerEntity player)
    {
        owner = player.getUuid();
    }
    public UUID getOwner() {
        return owner;
    }

    public void setCarriageHolder(@Nullable HorseEntity carriageHolder) {
        this.carriageHolder = carriageHolder;
    }
    public @Nullable HorseEntity getCarriageHolder() {
        return carriageHolder;
    }

    @Override
    protected void tickNewAi() {
        if(carriageHolder == null)
            return;

        Vec3d delta = getPos().subtract(carriageHolder.getPos());
        double yaw = Math.toDegrees(Math.atan2(delta.getY(), delta.x)) + 90D;
        boolean shouldMove = delta.lengthSquared() > FOLLOW_DISTANCE * FOLLOW_DISTANCE;

        setRotation((float) yaw, 0.f);
        setHeadYaw((float) yaw);
    }

    @Override
    public void tick() {
        super.tick();
        this.bodyYaw = yaw;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        if(tag.contains("owner"))
            owner = tag.getUuid("owner");

        if(tag.contains("carriageHolder"))
        {
            UUID carriageHolderId = tag.getUuid("carriageHolder");

            //Vec3d pos = getPos();
            //List<HorseEntity> horses = this.world.getEntitiesByType(EntityType.HORSE, new Box(pos.x - 5, pos.y - 5, pos.z -5, pos.x + 5, pos.y +5, pos.z + 5), horseEntity -> { return horseEntity.getUuid() == carriageHolderId;});
            //if(!horses.isEmpty())
            //    carriageHolder = horses.get(0);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        if(owner != null)
            tag.putUuid("owner", owner);
        if(carriageHolder != null)
            tag.putUuid("carriageHolder", carriageHolder.getUuid());

        return tag;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() { return DefaultedList.of(); }
    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) { return ItemStack.EMPTY; }
    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) { }
    @Override
    public Arm getMainArm() { return Arm.LEFT; }
}

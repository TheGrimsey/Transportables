package net.thegrimsey.transportables.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CarriageEntity extends LivingEntity {
    @Nullable
    HorseEntity carriageHolder = null;

    UUID owner;

    final float FOLLOW_DISTANCE = 4F;
    final int MAX_PASSENGERS = 4;

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

        if(shouldMove)
        {
            double distanceToMove = delta.length() - FOLLOW_DISTANCE;

            move(MovementType.SELF, delta.normalize().multiply(distanceToMove * -1D));
        }

        setRotation((float) yaw, 0.f);
        setHeadYaw((float) yaw);
    }

    @Override
    public void tick() {
        super.tick();
        this.bodyYaw = yaw;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {

        // Server
        if(!this.world.isClient())
        {
            // Start riding.
            return player.startRiding(this) ? ActionResult.SUCCESS : ActionResult.FAIL;
        }

        return super.interact(player, hand);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return getPassengerList().size() < MAX_PASSENGERS;
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

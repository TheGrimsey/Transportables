package net.thegrimsey.transportables.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.thegrimsey.transportables.TransportablesEntities;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class CarriageEntity extends LivingEntity {
    @Nullable
    HorseEntity carriageHolder = null;

    UUID owner;

    final float FOLLOW_DISTANCE = 2.5F;
    final int MAX_PASSENGERS = 4;

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
        this.stepHeight = 1F;
        this.shouldRenderName()
        Objects.requireNonNull(getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)).setBaseValue(0.95D);
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

        // Only move when we are out of follow distance. This allows us to just rotate if needed which looks NICE.
        boolean shouldMove = delta.lengthSquared() > FOLLOW_DISTANCE * FOLLOW_DISTANCE;
        if(shouldMove)
        {
            // We only want to move up to our follow distance.
            double distanceToMove = delta.length() - FOLLOW_DISTANCE;

            move(MovementType.SELF, delta.normalize().multiply(distanceToMove * -1D));
        }

        double yaw = Math.toDegrees(Math.atan2(delta.z, delta.x)) + 90D;
        setRotation((float) yaw, 0.f);
        setHeadYaw((float) yaw);
    }

    @Override
    public void tick() {
        super.tick();

        // Allow horse rider to predict the movement of the carriage.
        // This looks nicer most of the time. Still some issues with going up blocks.
        if(this.world.isClient() && carriageHolder != null)
        {
            if(carriageHolder.getPrimaryPassenger() == MinecraftClient.getInstance().player)
            {
                tickNewAi();
            }
        }

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
    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            int i = this.getPassengerList().indexOf(passenger);
            float x = 0.5F - ((i / 2) * 1.2F);
            float z = -0.7F + (i % 2) * 1.3F;

            Vec3d vec3d = (new Vec3d(x, 0.0D, z)).rotateY(-this.yaw * 0.017453292F - 1.5707964F);
            passenger.updatePosition(this.getX() + vec3d.x, this.getY() + 0.1F, this.getZ() + vec3d.z);
            copyEntityData(passenger);
        }
    }

    @Environment(EnvType.CLIENT)
    public void onPassengerLookAround(Entity entity) {
        copyEntityData(entity);
    }

    private void copyEntityData(Entity entity) {
        float rot = 90F * (getPassengerList().indexOf(entity) % 2 == 0 ? -1F : 1F);

        entity.setYaw(this.yaw + rot);
        float f = MathHelper.wrapDegrees(entity.yaw - this.yaw + rot);
        float g = MathHelper.clamp(f, -89.9F, 89.9F);
        entity.prevYaw += g - f;
        entity.yaw += g - f;
        entity.setHeadYaw(entity.yaw);
    }

    @Nullable
    @Override
    public Entity getPrimaryPassenger() {
        return null;
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

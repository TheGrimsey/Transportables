package net.thegrimsey.transportables.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractCarriageEntity extends LivingEntity {
    static final TrackedData<Optional<UUID>> CARRIAGE_HOLDER = DataTracker.registerData(CarriageEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    final float FOLLOW_DISTANCE = 2.75F;
    final int MAX_PASSENGERS = 4;

    @Nullable
    HorseBaseEntity carriageHolder = null;

    protected AbstractCarriageEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1F;
        Objects.requireNonNull(getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)).setBaseValue(0.95D);
    }

    public void setCarriageHolder(HorseBaseEntity carriageHolder) {
        this.dataTracker.set(CARRIAGE_HOLDER, Optional.of(carriageHolder.getUuid()));
        this.carriageHolder = carriageHolder;
    }
    public void removeCarriageHolder() {
        this.dataTracker.set(CARRIAGE_HOLDER, Optional.empty());
        this.carriageHolder = null;
    }

    @Override
    public void tick() {
        super.tick();

        if(carriageHolder != null) {
            if(!carriageHolder.isAlive())
                removeCarriageHolder();
        }

        if(this.world.isClient)
        {
            Optional<UUID> holderId = this.dataTracker.get(CARRIAGE_HOLDER);
            if(holderId.isPresent())
            {
                Entity playerVehicle = MinecraftClient.getInstance().player.getVehicle();
                if(playerVehicle != null && playerVehicle.getUuid().equals(holderId.get())) {
                    carriageHolder = (HorseBaseEntity) playerVehicle;
                    System.out.println("Riding it.");
                    tickCarriageMovement();
                }
            }
        }

        this.bodyYaw = this.yaw;
    }

    @Override
    protected void tickNewAi() {
        tickCarriageMovement();
    }

    protected void tickCarriageMovement() {
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
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (!this.world.isClient()) {
            return player.startRiding(this) ? ActionResult.SUCCESS : ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            int i = this.getPassengerList().indexOf(passenger);
            double x = 0.5F - ((i >> 1) * 1.2F);
            double z = -0.7F + (i % 2) * 1.3F;

            // Rotate position
            float angle = -this.yaw * 0.017453292F - 1.5707964F;
            double f = MathHelper.cos(angle);
            double g = MathHelper.sin(angle);
            double rX = x * f + z * g;
            double rZ = z * f - x * g;

            passenger.updatePosition(this.getX() + rX, this.getY() + 0.1F, this.getZ() + rZ);
            updatePassengerRotation(passenger);
        }
    }

    @Environment(EnvType.CLIENT)
    public void onPassengerLookAround(Entity entity) {
        updatePassengerRotation(entity);
    }

    private void updatePassengerRotation(Entity entity) {
        float rot = 90F * (getPassengerList().indexOf(entity) % 2 == 0 ? -1F : 1F);

        entity.setYaw(this.yaw + rot);
        float f = MathHelper.wrapDegrees(entity.yaw - this.yaw + rot);
        float g = MathHelper.clamp(f, -89.9F, 89.9F);
        entity.prevYaw += g - f;
        entity.yaw += g - f;
    }


    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        if(tag.contains("carriageHolder"))
        {
            UUID carriageHolderId = tag.getUuid("carriageHolder");

            Entity carriageHolder = ((ServerWorld)this.world).getEntity(carriageHolderId);
            if(carriageHolder instanceof HorseEntity)
                setCarriageHolder((HorseEntity) carriageHolder);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        if(carriageHolder != null)
            tag.putUuid("carriageHolder", carriageHolder.getUuid());

        return tag;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CARRIAGE_HOLDER, Optional.empty());
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return getPassengerList().size() < MAX_PASSENGERS;
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

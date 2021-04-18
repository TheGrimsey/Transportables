package net.thegrimsey.transportables.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.thegrimsey.transportables.TransportablesEntities;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class ChestCarriageEntity extends AbstractCarriageEntity implements Inventory, NamedScreenHandlerFactory {
    private DefaultedList<ItemStack> inventory;

    final float REACH_DISTANCE = 5F;
    final int MAX_PASSENGERS = 2;

    public static ChestCarriageEntity create(World world, double x, double y, double z, float yaw)
    {
        ChestCarriageEntity carriage = new ChestCarriageEntity(TransportablesEntities.CHEST_CARRIAGE, world);
        carriage.updatePosition(x, y, z);
        carriage.setVelocity(Vec3d.ZERO);
        carriage.prevX = x;
        carriage.prevY = y;
        carriage.prevZ = z;

        carriage.yaw = yaw;

        return carriage;
    }

    public ChestCarriageEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        this.inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
    }

    @Override
    public int size() {
        return 9*6;
    }

    public boolean isEmpty() {
        Iterator<ItemStack> itr = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!itr.hasNext()) {
                return true;
            }

            itemStack = itr.next();
        } while(itemStack.isEmpty());

        return false;
    }

    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    public ItemStack removeStack(int slot) {
        ItemStack itemStack = this.inventory.get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.inventory.set(slot, ItemStack.EMPTY);
            return itemStack;
        }
    }

    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.removed) {
            return false;
        } else {
            return !(player.squaredDistanceTo(this) > (REACH_DISTANCE*REACH_DISTANCE));
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ItemStack handStack = player.getStackInHand(hand);
        if(!handStack.isEmpty())
        {
            ActionResult result =  handStack.useOnEntity(player, this, hand);
            if(result.isAccepted())
                return result;
        }

        if(player.isSneaking()) {
            player.openHandledScreen(this);
            return ActionResult.SUCCESS;
        }

        return super.interact(player, hand);
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            int i = this.getPassengerList().indexOf(passenger);
            double x = -0.7F;
            double z = -0.7F + (i % 2) * 1.3F;

            // Rotate position
            float angle = -this.yaw * 0.017453292F - 1.5707964F;
            double f = MathHelper.cos(angle);
            double g = MathHelper.sin(angle);
            double rX = x * f + z * g;
            double rZ = z * f - x * g;

            passenger.updatePosition(this.getX() + rX, this.getY() + SEATING_Y_OFFSET, this.getZ() + rZ);
            updatePassengerRotation(passenger);
        }
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return getPassengerList().size() < MAX_PASSENGERS;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return GenericContainerScreenHandler.createGeneric9x6(syncId, inv, this);
    }

    @Override
    public void clear() {
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        Inventories.toTag(tag, this.inventory);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        Inventories.fromTag(tag, this.inventory);
    }

    @Override
    protected void drop(DamageSource source) {
        super.drop(source);

        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            // Drop chest.
            dropStack(new ItemStack(Blocks.CHEST, 2));

            // Drop inventory.
            for (ItemStack itemStack : this.inventory) {
                dropStack(itemStack);
            }
        }
    }
}

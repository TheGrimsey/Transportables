package net.thegrimsey.transportables.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.world.World;
import net.thegrimsey.transportables.TransportablesItems;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class ChestCarriageEntity extends AbstractCarriageEntity implements Inventory, NamedScreenHandlerFactory {
    private DefaultedList<ItemStack> inventory;

    final float REACH_DISTANCE = 5F;

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
        if(player.isSneaking() && player.getMainHandStack().getItem() != TransportablesItems.LINKER_ITEM) {
            player.openHandledScreen(this);
            return ActionResult.SUCCESS;
        }

        return super.interact(player, hand);
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
}

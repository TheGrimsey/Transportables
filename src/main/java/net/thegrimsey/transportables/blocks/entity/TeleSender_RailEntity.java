package net.thegrimsey.transportables.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.thegrimsey.transportables.TransportablesBlocks;

public class TeleSender_RailEntity extends BlockEntity {
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private boolean destinationSet = false;

    public TeleSender_RailEntity() {
        super(TransportablesBlocks.TELESENDER_RAILENTITY);
    }

    public void setDestination(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        destinationSet = true;
    }

    public boolean hasDestination() { return destinationSet; }
    public int getDestinationX() { return x; }
    public int getDestinationY() { return y; }
    public int getDestinationZ() { return z; }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        if(destinationSet)
        {
            tag.putInt("X", x);
            tag.putInt("Y", y);
            tag.putInt("Z", z);
        }

        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        if(tag.contains("X"))
            setDestination(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
    }
}

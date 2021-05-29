package net.thegrimsey.transportables.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.thegrimsey.transportables.TransportablesBlocks;

public class TeleSender_RailEntity extends BlockEntity {
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private boolean destinationSet = false;

    public TeleSender_RailEntity(BlockPos pos, BlockState state) {
        super(TransportablesBlocks.TELESENDER_RAILENTITY, pos, state);
    }

    public void setDestination(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        destinationSet = true;
    }

    public boolean hasDestination() {
        return destinationSet;
    }

    public int getDestinationX() {
        return x;
    }

    public int getDestinationY() {
        return y;
    }

    public int getDestinationZ() {
        return z;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        if (destinationSet) {
            tag.putInt("X", x);
            tag.putInt("Y", y);
            tag.putInt("Z", z);
        }

        return tag;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if (tag.contains("X"))
            setDestination(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
    }
}

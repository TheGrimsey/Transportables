package net.thegrimsey.transportables.blocks;

import net.minecraft.block.LadderBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IronLadder extends LadderBlock {
    public static final double climbSpeed = 0.5D;

    public IronLadder(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("transportables.iron_ladder.tooltip_01"));

        super.appendTooltip(stack, world, tooltip, options);
    }
}

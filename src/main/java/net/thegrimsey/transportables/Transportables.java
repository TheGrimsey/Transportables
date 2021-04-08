package net.thegrimsey.transportables;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.thegrimsey.transportables.blocks.HighPowered_Rail;
import net.thegrimsey.transportables.blocks.Launching_Rail;

public class Transportables implements ModInitializer {
	public static final String MODID = "transportables";

	@Override
	public void onInitialize() {
		TransportablesBlocks.RegisterBlocks();
	}
}

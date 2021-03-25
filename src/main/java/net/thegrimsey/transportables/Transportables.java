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

public class Transportables implements ModInitializer {
	public static final String MODID = "transportables";
	public static final HighPowered_Rail HIGHPOWERED_RAIL = new HighPowered_Rail(FabricBlockSettings.of(Material.METAL).strength(4.0f).nonOpaque().noCollision());

	@Override
	public void onInitialize() {
		Identifier HIGHPOWERED_RAIL_ID = new Identifier(MODID, "highpowered_rail");
		Registry.register(Registry.BLOCK, HIGHPOWERED_RAIL_ID, HIGHPOWERED_RAIL);
		Registry.register(Registry.ITEM, HIGHPOWERED_RAIL_ID, new BlockItem(HIGHPOWERED_RAIL, new FabricItemSettings().group(ItemGroup.TRANSPORTATION)));

	}
}

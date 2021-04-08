package net.thegrimsey.transportables;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.thegrimsey.transportables.blocks.HighPowered_Rail;
import net.thegrimsey.transportables.blocks.Launching_Rail;

public class TransportablesBlocks {
    public static final FabricBlockSettings RAIL_SETTINGS = FabricBlockSettings.of(Material.METAL).strength(1.0f).nonOpaque().noCollision();
    public static final HighPowered_Rail HIGHPOWERED_RAIL = new HighPowered_Rail(RAIL_SETTINGS);
    public static final Launching_Rail LAUNCHING_RAIL = new Launching_Rail(RAIL_SETTINGS);

    public static void RegisterBlocks()
    {
        RegisterBlock("highpowered_rail", HIGHPOWERED_RAIL);
        RegisterBlock("launching_rail", LAUNCHING_RAIL);
    }

    private static void RegisterBlock(String Id, Block block)
    {
        Identifier identifier = new Identifier(Transportables.MODID, Id.toLowerCase());
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, new FabricItemSettings().group(ItemGroup.TRANSPORTATION)));

    }
}

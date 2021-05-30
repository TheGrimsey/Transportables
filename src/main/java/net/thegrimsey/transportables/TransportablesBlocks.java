package net.thegrimsey.transportables;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.thegrimsey.transportables.blocks.HighPowered_Rail;
import net.thegrimsey.transportables.blocks.IronLadder;
import net.thegrimsey.transportables.blocks.Launching_Rail;
import net.thegrimsey.transportables.blocks.TeleSender_Rail;
import net.thegrimsey.transportables.blocks.entity.TeleSender_RailEntity;

public class TransportablesBlocks {
    public static final FabricBlockSettings RAIL_SETTINGS = FabricBlockSettings.of(Material.DECORATION).nonOpaque().noCollision().strength(0.7F).sounds(BlockSoundGroup.METAL);
    public static final HighPowered_Rail HIGHPOWERED_RAIL = new HighPowered_Rail(RAIL_SETTINGS);
    public static final Launching_Rail LAUNCHING_RAIL = new Launching_Rail(RAIL_SETTINGS);
    public static final TeleSender_Rail TELESENDER_RAIL = new TeleSender_Rail(RAIL_SETTINGS);

    public static final IronLadder IRON_LADDER = new IronLadder(FabricBlockSettings.of(Material.METAL).nonOpaque().strength(0.7F).sounds(BlockSoundGroup.METAL));

    // Block Entities
    public static BlockEntityType<TeleSender_RailEntity> TELESENDER_RAILENTITY;

    public static void RegisterBlocks()
    {
        RegisterBlock("highpowered_rail", HIGHPOWERED_RAIL);
        RegisterBlock("launching_rail", LAUNCHING_RAIL);
        RegisterBlock("telesender_rail", TELESENDER_RAIL);

        RegisterBlock("iron_ladder", IRON_LADDER);

        TELESENDER_RAILENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Transportables.MODID, "telesender_railentity"), FabricBlockEntityTypeBuilder.create(TeleSender_RailEntity::new, TELESENDER_RAIL).build(null));
    }

    private static void RegisterBlock(String Id, Block block)
    {
        Identifier identifier = new Identifier(Transportables.MODID, Id.toLowerCase());
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, new FabricItemSettings().group(ItemGroup.TRANSPORTATION)));

        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            RegisterBlockRenderLayerMap(block);
    }

    @Environment(EnvType.CLIENT)
    private static void RegisterBlockRenderLayerMap(Block block)
    {
        BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());

    }
}

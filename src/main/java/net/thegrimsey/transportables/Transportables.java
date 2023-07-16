package net.thegrimsey.transportables;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;

public class Transportables implements ModInitializer {
    public static final String MODID = "transportables";
    public static TransportablesConfig CONFIG;

    @Override
    public void onInitialize() {
        AutoConfig.register(TransportablesConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(TransportablesConfig.class).getConfig();

        TransportablesBlocks.RegisterBlocks();
        TransportablesItems.RegisterItems();
        TransportablesEntities.RegisterEntities();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(TransportablesItems.LINKER_ITEM);
            entries.add(TransportablesItems.CARRIAGE_ITEM);
            entries.add(TransportablesItems.CHEST_CARRIAGE_ITEM);

            entries.add(TransportablesBlocks.HIGHPOWERED_RAIL);
            entries.add(TransportablesBlocks.TELESENDER_RAIL);
            entries.add(TransportablesBlocks.IRON_LADDER);
            entries.add(TransportablesBlocks.LAUNCHING_RAIL);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(TransportablesBlocks.HIGHPOWERED_RAIL);
            entries.add(TransportablesBlocks.TELESENDER_RAIL);
            entries.add(TransportablesBlocks.LAUNCHING_RAIL);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(TransportablesBlocks.IRON_LADDER);
        });
    }
}

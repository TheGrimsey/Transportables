package net.thegrimsey.transportables;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class Transportables implements ModInitializer {
	public static final String MODID = "transportables";
	public static TransportablesConfig CONFIG;

	@Override
	public void onInitialize() {
		AutoConfig.register(TransportablesConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(TransportablesConfig.class).getConfig();

		TransportablesBlocks.RegisterBlocks();
		TransportablesItems.RegisterItems();
	}
}

package net.thegrimsey.transportables;

import net.fabricmc.api.ModInitializer;

public class Transportables implements ModInitializer {
	public static final String MODID = "transportables";

	@Override
	public void onInitialize() {
		TransportablesBlocks.RegisterBlocks();
		TransportablesItems.RegisterItems();
	}
}

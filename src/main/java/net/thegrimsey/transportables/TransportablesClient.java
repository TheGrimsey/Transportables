package net.thegrimsey.transportables;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.thegrimsey.transportables.entity.CarriageEntityRenderer;
import net.thegrimsey.transportables.entity.ChestCarriageEntityRenderer;

@Environment(EnvType.CLIENT)
public class TransportablesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(TransportablesEntities.CARRIAGE, (dispatcher, context) -> new CarriageEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(TransportablesEntities.CHEST_CARRIAGE, (dispatcher, context) -> new ChestCarriageEntityRenderer(dispatcher));
    }
}

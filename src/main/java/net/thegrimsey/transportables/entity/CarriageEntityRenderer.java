package net.thegrimsey.transportables.entity;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;
import net.thegrimsey.transportables.Transportables;

public class CarriageEntityRenderer extends LivingEntityRenderer<CarriageEntity, CarriageEntityModel> {

    public CarriageEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new CarriageEntityModel(), 1.5f);
    }

    @Override
    public Identifier getTexture(CarriageEntity entity) {
        return new Identifier(Transportables.MODID, "textures/entity/carriage.png");
    }
}

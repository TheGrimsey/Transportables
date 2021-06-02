package net.thegrimsey.transportables.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;
import net.thegrimsey.transportables.Transportables;

public class CarriageEntityRenderer extends LivingEntityRenderer<AbstractCarriageEntity, CarriageEntityModel> {
    public static final Identifier TEXTURE = new Identifier(Transportables.MODID, "textures/entity/carriage.png");

    public CarriageEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CarriageEntityModel(), AbstractCarriageEntity.CARRIAGE_DIMENSIONS.width / 2F);
    }

    @Override
    protected boolean hasLabel(AbstractCarriageEntity carriageEntity) {
        return super.hasLabel(carriageEntity) && (carriageEntity.shouldRenderName() || carriageEntity.hasCustomName() && carriageEntity == this.dispatcher.targetedEntity);
    }

    @Override
    public Identifier getTexture(AbstractCarriageEntity entity) {
        return TEXTURE;
    }
}

package net.thegrimsey.transportables.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;
import net.thegrimsey.transportables.Transportables;

public class ChestCarriageEntityRenderer extends LivingEntityRenderer<AbstractCarriageEntity, CarriageEntityModel> {
    public ChestCarriageEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CarriageEntityModel(), AbstractCarriageEntity.CARRIAGE_DIMENSIONS.width / 2F);
        this.addFeature(new CarriageChestFeatureRenderer(this));
    }

    @Override
    protected boolean hasLabel(AbstractCarriageEntity carriageEntity) {
        return super.hasLabel(carriageEntity) && (carriageEntity.shouldRenderName() || carriageEntity.hasCustomName() && carriageEntity == this.dispatcher.targetedEntity);
    }

    @Override
    public Identifier getTexture(AbstractCarriageEntity entity) {
        return new Identifier(Transportables.MODID, "textures/entity/carriage.png");
    }
}

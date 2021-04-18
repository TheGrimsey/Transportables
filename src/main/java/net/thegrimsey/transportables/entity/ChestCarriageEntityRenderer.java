package net.thegrimsey.transportables.entity;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;
import net.thegrimsey.transportables.Transportables;

public class ChestCarriageEntityRenderer extends LivingEntityRenderer<ChestCarriageEntity, ChestCarriageEntityModel> {
    public ChestCarriageEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ChestCarriageEntityModel(), AbstractCarriageEntity.CARRIAGE_DIMENSIONS.width / 2F);
    }

    @Override
    protected boolean hasLabel(ChestCarriageEntity carriageEntity) {
        return super.hasLabel(carriageEntity) && (carriageEntity.shouldRenderName() || carriageEntity.hasCustomName() && carriageEntity == this.dispatcher.targetedEntity);
    }

    @Override
    public Identifier getTexture(ChestCarriageEntity entity) {
        return new Identifier(Transportables.MODID, "textures/entity/carriage.png");
    }
}

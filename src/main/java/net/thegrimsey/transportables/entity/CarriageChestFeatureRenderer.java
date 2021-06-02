package net.thegrimsey.transportables.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;

public class CarriageChestFeatureRenderer extends FeatureRenderer<AbstractCarriageEntity, CarriageEntityModel> {
    private final ModelPart model;

    public CarriageChestFeatureRenderer(FeatureRendererContext<AbstractCarriageEntity, CarriageEntityModel> context) {
        super(context);

        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelTransform mainCartPivot = ModelTransform.pivot(0.0F, 24.0F, 0.0F);
        modelPartData.addChild("mainCartChest", ModelPartBuilder.create().uv(168, 0).cuboid(-15.0F, -22.0F, -18.0F, 30.0F, 14.0F, 14.0F, false), mainCartPivot);
        modelPartData.addChild("mainCartChestHatch", ModelPartBuilder.create().uv(249, 29).cuboid(-1.0F, -19.0F, -4.0F, 2.0F, 4.0F, 1.0F, false), mainCartPivot);

        model = modelPartData.createPart(256, 256);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractCarriageEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(CarriageEntityRenderer.TEXTURE));
        model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0F));
    }
}

package net.thegrimsey.transportables.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class CarriageEntityModel extends EntityModel<CarriageEntity> {

    private final ModelPart mainCart;

    public CarriageEntityModel()
    {
        this.textureHeight = 128;
        this.textureWidth = 128;

        mainCart = new ModelPart(this, 0, 0);

        mainCart.setPivot(0f,24f,0f);
        mainCart.setTextureOffset(0, 0).addCuboid(-16.0F, -2.0F, -20.0F, 32.0F, 2.0F, 40.0F, 0.0F, false);
        mainCart.setTextureOffset(0, 44).addCuboid(-18.0F, -14.0F, -20.0F, 2.0F, 14.0F, 40.0F, 0.0F, true);
        mainCart.setTextureOffset(0, 44).addCuboid(16.0F, -14.0F, -20.0F, 2.0F, 14.0F, 40.0F, 0.0F, false);
        mainCart.setTextureOffset(0, 112).addCuboid(-16.0F, -14.0F, -20.0F, 32.0F, 14.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setAngles(CarriageEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        mainCart.render(matrices, vertices, light, overlay);
    }
}

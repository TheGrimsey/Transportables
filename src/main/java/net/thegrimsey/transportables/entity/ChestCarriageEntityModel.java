package net.thegrimsey.transportables.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class ChestCarriageEntityModel extends EntityModel<ChestCarriageEntity> {

    private final ModelPart mainCart;
    private final ModelPart frontAxel;
    private final ModelPart backAxel;

    public ChestCarriageEntityModel()
    {
        this.textureHeight = 256;
        this.textureWidth = 256;

        mainCart = new ModelPart(this);
        mainCart.setPivot(0.0F, 24.0F, 0.0F);
        mainCart.setTextureOffset(0, 0).addCuboid(-16.0F, -8.0F, -20.0F, 32.0F, 2.0F, 40.0F, 0.0F, false);
        mainCart.setTextureOffset(0, 44).addCuboid(-18.0F, -20.0F, -20.0F, 2.0F, 14.0F, 40.0F, 0.0F, true);
        mainCart.setTextureOffset(0, 44).addCuboid(16.0F, -20.0F, -20.0F, 2.0F, 14.0F, 40.0F, 0.0F, false);
        mainCart.setTextureOffset(0, 112).addCuboid(-16.0F, -20.0F, -20.0F, 32.0F, 14.0F, 2.0F, 0.0F, false);

        frontAxel = new ModelPart(this);
        frontAxel.setPivot(0.0F, 19.5F, -13.5F);
        frontAxel.setTextureOffset(0, 130).addCuboid(-18.0F, -0.5F, -0.5F, 36.0F, 1.0F, 1.0F, 0.0F, false);
        frontAxel.setTextureOffset(112, 51).addCuboid(-19.0F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, 0.0F, false);
        frontAxel.setTextureOffset(112, 66).addCuboid(-19.0F, -2.5F, 3.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        frontAxel.setTextureOffset(116, 72).addCuboid(-19.0F, -4.5F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        frontAxel.setTextureOffset(116, 79).addCuboid(-19.0F, 3.5F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        frontAxel.setTextureOffset(116, 66).addCuboid(-19.0F, -2.5F, -4.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        frontAxel.setTextureOffset(112, 51).addCuboid(18.0F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, 0.0F, true);
        frontAxel.setTextureOffset(112, 66).addCuboid(18.0F, -2.5F, 3.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
        frontAxel.setTextureOffset(116, 72).addCuboid(18.0F, -4.5F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, true);
        frontAxel.setTextureOffset(116, 79).addCuboid(18.0F, 3.5F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, true);
        frontAxel.setTextureOffset(116, 66).addCuboid(18.0F, -2.5F, -4.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);

        backAxel = new ModelPart(this);
        backAxel.setPivot(0.0F, 19.5F, 14.5F);
        backAxel.setTextureOffset(0, 130).addCuboid(-18.0F, -0.5F, -0.5F, 36.0F, 1.0F, 1.0F, 0.0F, false);
        backAxel.setTextureOffset(112, 51).addCuboid(-19.0F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, 0.0F, false);
        backAxel.setTextureOffset(112, 66).addCuboid(-19.0F, -2.5F, 3.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        backAxel.setTextureOffset(116, 72).addCuboid(-19.0F, -4.5F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        backAxel.setTextureOffset(116, 79).addCuboid(-19.0F, 3.5F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        backAxel.setTextureOffset(116, 66).addCuboid(-19.0F, -2.5F, -4.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        backAxel.setTextureOffset(112, 51).addCuboid(18.0F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, 0.0F, true);
        backAxel.setTextureOffset(112, 66).addCuboid(18.0F, -2.5F, 3.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
        backAxel.setTextureOffset(116, 72).addCuboid(18.0F, -4.5F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, true);
        backAxel.setTextureOffset(116, 79).addCuboid(18.0F, 3.5F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, true);
        backAxel.setTextureOffset(116, 66).addCuboid(18.0F, -2.5F, -4.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);
    }

    @Override
    public void setAngles(ChestCarriageEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        final float circumference = (7*4);
        Vec3d forwardMovement = entity.getVelocity().rotateY(-entity.pitch);
        float rot = (float) Math.toRadians((-forwardMovement.x / circumference) * 360F);
        frontAxel.pitch += rot;
        backAxel.pitch += rot;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        mainCart.render(matrices, vertices, light, overlay);
        frontAxel.render(matrices, vertices, light, overlay);
        backAxel.render(matrices, vertices, light, overlay);
    }
}

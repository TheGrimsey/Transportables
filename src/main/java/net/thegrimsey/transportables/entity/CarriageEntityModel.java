package net.thegrimsey.transportables.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class CarriageEntityModel extends EntityModel<CarriageEntity> {

    private final ModelPart mainCart;
    private final ModelPart frontAxel;
    private final ModelPart backAxel;

    public CarriageEntityModel()
    {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelTransform mainCartPivot = ModelTransform.pivot(0.0F, 24.0F, 0.0F);
        modelPartData.addChild("mainCartBottom", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -8.0F, -20.0F, 32.0F, 2.0F, 40.0F, false), mainCartPivot);
        modelPartData.addChild("mainCartRight", ModelPartBuilder.create().uv(0, 44).cuboid(-18.0F, -20.0F, -20.0F, 2.0F, 14.0F, 40.0F, true), mainCartPivot);
        modelPartData.addChild("mainCartLeft", ModelPartBuilder.create().uv(0, 44).cuboid(16.0F, -20.0F, -20.0F, 2.0F, 14.0F, 40.0F, false), mainCartPivot);
        modelPartData.addChild("mainCartBack", ModelPartBuilder.create().uv(0, 112).cuboid(-16.0F, -20.0F, -20.0F, 32.0F, 14.0F, 2.0F, false), mainCartPivot);

        mainCart = modelPartData.createPart(256, 256);
        modelData = new ModelData();
        modelPartData = modelData.getRoot();

        ModelTransform frontAxelPivot = ModelTransform.pivot(0.0F, 19.5F, -13.5F);
        modelPartData.addChild("FrontAxel", ModelPartBuilder.create().uv(0, 130).cuboid(-18.0F, -0.5F, -0.5F, 36.0F, 1.0F, 1.0F, false), frontAxelPivot);

        modelPartData.addChild("FrontLeftWheel", ModelPartBuilder.create().uv(112, 51).cuboid(-19.0F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, false), frontAxelPivot);
        modelPartData.addChild("FrontLeftWheel1", ModelPartBuilder.create().uv(112, 66).cuboid(-19.0F, -2.5F, 3.5F, 1.0F, 5.0F, 1.0F, false), frontAxelPivot);
        modelPartData.addChild("FrontLeftWheel2", ModelPartBuilder.create().uv(116, 72).cuboid(-19.0F, -4.5F, -2.5F, 1.0F, 1.0F, 5.0F, false), frontAxelPivot);
        modelPartData.addChild("FrontLeftWheel3", ModelPartBuilder.create().uv(116, 79).cuboid(-19.0F, 3.5F, -2.5F, 1.0F, 1.0F, 5.0F, false), frontAxelPivot);
        modelPartData.addChild("FrontLeftWheel4", ModelPartBuilder.create().uv(116, 66).cuboid(-19.0F, -2.5F, -4.5F, 1.0F, 5.0F, 1.0F, false), frontAxelPivot);

        modelPartData.addChild("FrontRightWheel", ModelPartBuilder.create().uv(112, 51).cuboid(18.0F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, true), frontAxelPivot);
        modelPartData.addChild("FrontRightWheel1", ModelPartBuilder.create().uv(112, 66).cuboid(18.0F, -2.5F, 3.5F, 1.0F, 5.0F, 1.0F, true), frontAxelPivot);
        modelPartData.addChild("FrontRightWheel2", ModelPartBuilder.create().uv(116, 72).cuboid(18.0F, -4.5F, -2.5F, 1.0F, 1.0F, 5.0F, true), frontAxelPivot);
        modelPartData.addChild("FrontRightWheel3", ModelPartBuilder.create().uv(116, 79).cuboid(18.0F, 3.5F, -2.5F, 1.0F, 1.0F, 5.0F, true), frontAxelPivot);
        modelPartData.addChild("FrontRightWheel4", ModelPartBuilder.create().uv(116, 66).cuboid(18.0F, -2.5F, -4.5F, 1.0F, 5.0F, 1.0F, true), frontAxelPivot);

        frontAxel = modelPartData.createPart(256, 256);
        modelData = new ModelData();
        modelPartData = modelData.getRoot();

        ModelTransform backAxelPivot = ModelTransform.pivot(0.0F, 19.5F, 14.5F);
        modelPartData.addChild("BackAxel", ModelPartBuilder.create().uv(0, 130).cuboid(-18.0F, -0.5F, -0.5F, 36.0F, 1.0F, 1.0F, false), frontAxelPivot);

        modelPartData.addChild("BackLeftWheel", ModelPartBuilder.create().uv(112, 51).cuboid(-19.0F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, false), backAxelPivot);
        modelPartData.addChild("BackLeftWheel1", ModelPartBuilder.create().uv(112, 66).cuboid(-19.0F, -2.5F, 3.5F, 1.0F, 5.0F, 1.0F, false), backAxelPivot);
        modelPartData.addChild("BackLeftWheel2", ModelPartBuilder.create().uv(116, 72).cuboid(-19.0F, -4.5F, -2.5F, 1.0F, 1.0F, 5.0F, false), backAxelPivot);
        modelPartData.addChild("BackLeftWheel3", ModelPartBuilder.create().uv(116, 79).cuboid(-19.0F, 3.5F, -2.5F, 1.0F, 1.0F, 5.0F, false), backAxelPivot);
        modelPartData.addChild("BackLeftWheel4", ModelPartBuilder.create().uv(116, 66).cuboid(-19.0F, -2.5F, -4.5F, 1.0F, 5.0F, 1.0F, false), backAxelPivot);

        modelPartData.addChild("BackRightWheel", ModelPartBuilder.create().uv(112, 51).cuboid(18.0F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, true), backAxelPivot);
        modelPartData.addChild("BackRightWheel1", ModelPartBuilder.create().uv(112, 66).cuboid(18.0F, -2.5F, 3.5F, 1.0F, 5.0F, 1.0F, true), backAxelPivot);
        modelPartData.addChild("BackRightWheel2", ModelPartBuilder.create().uv(116, 72).cuboid(18.0F, -4.5F, -2.5F, 1.0F, 1.0F, 5.0F, true), backAxelPivot);
        modelPartData.addChild("BackRightWheel3", ModelPartBuilder.create().uv(116, 79).cuboid(18.0F, 3.5F, -2.5F, 1.0F, 1.0F, 5.0F, true), backAxelPivot);
        modelPartData.addChild("BackRightWheel4", ModelPartBuilder.create().uv(116, 66).cuboid(18.0F, -2.5F, -4.5F, 1.0F, 5.0F, 1.0F, true), backAxelPivot);

        backAxel = modelPartData.createPart(256, 256);
    }

    @Override
    public void setAngles(CarriageEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        final float circumference = (7*4);
        Vec3d forwardMovement = entity.getVelocity().rotateY(-entity.getPitch());
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

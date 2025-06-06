package net.pawlrip.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.ShulkerBulletEntityModel;
import net.minecraft.entity.Entity;
import net.pawlrip.entity.custom.RayEntity;

@Environment(EnvType.CLIENT)
public class RayEntityModel<T extends Entity> extends ShulkerBulletEntityModel<RayEntity> {

    private static final String MAIN = "main";
    private final ModelPart rectangle;

    public RayEntityModel(ModelPart root) {
        super(root);
        this.rectangle = root.getChild(MAIN);
    }

    public static TexturedModelData getTexturedModelData(Shape shape) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        int textureX = 0; int textureY = 0;
        float offsetX = 0; float offsetY = 0; float offsetZ = 0;
        float sizeX = 0; float sizeY = 0; float sizeZ = 0;
        int textureWidth = 256; int textureHeight = 256;

        switch (shape) {
            case RAY -> {
                offsetX = -1.0F; offsetY = -4.0F; offsetZ = -4.0F;
                sizeX = 2.0F; sizeY = 2.0F; sizeZ = 8.0F;
            }
            case WALL -> {
                offsetX = -12.0F; offsetY = -36.0F; offsetZ = -0.5F;
                sizeX = 24.0F; sizeY = 36.0F; sizeZ = 1.0F;
            }
            case CUBE -> {
                offsetX = -4.0F; offsetY = -4.0F; offsetZ = -4.0F;
                sizeX = 2.0F; sizeY = 2.0F; sizeZ = 2.0F;
            }
            case INVISIBLE -> {
                offsetX = 0; offsetY = 0; offsetZ = 0;
                sizeX = 0; sizeY = 0; sizeZ = 0;
            }
        }

        modelPartData.addChild(MAIN, ModelPartBuilder.create().uv(textureX, textureY).cuboid(offsetX, offsetY, offsetZ,
                sizeX, sizeY, sizeZ), ModelTransform.NONE);
        return TexturedModelData.of(modelData, textureWidth, textureHeight);
    }

    public void setRectangleRotation(float yaw, float pitch) {
        // pi / 180 = 0.017453292F
        this.rectangle.yaw = yaw * 0.017453292F;
        this.rectangle.pitch = pitch * 0.017453292F;
    }

    public enum Shape {
        RAY,
        WALL,
        CUBE,
        INVISIBLE
    }
}

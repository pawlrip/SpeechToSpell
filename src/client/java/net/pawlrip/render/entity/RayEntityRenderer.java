package net.pawlrip.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.pawlrip.StSMain;
import net.pawlrip.entity.custom.RayEntity;
import net.pawlrip.render.entity.model.RayEntityModel;

@Environment(EnvType.CLIENT)
public class RayEntityRenderer extends EntityRenderer<RayEntity> {

    private static final Identifier TEXTURE = StSMain.id("textures/entity/white_plane.png");
    private final RayEntityModel<RayEntity> model;

    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;
    private final int blockLight; // set to -1 for default

    public RayEntityRenderer(EntityRendererFactory.Context context, EntityModelLayer modelLayer, float red, float green, float blue, float alpha, int blockLight) {
        super(context);
        this.model = new RayEntityModel<>(context.getPart(modelLayer));
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.blockLight = blockLight;
    }

    @Override
    public void render(RayEntity rayEntity, float yaw, float tick_delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(getTexture(rayEntity)));
        this.model.setRectangleRotation(rayEntity.getYaw(), rayEntity.getPitch());
        this.model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(RayEntity entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLight(RayEntity rayEntity, BlockPos blockPos) {
        return this.blockLight == -1 ? super.getBlockLight(rayEntity, blockPos) : this.blockLight;
    }
}

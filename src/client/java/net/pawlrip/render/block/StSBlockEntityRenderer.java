package net.pawlrip.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.pawlrip.StSMain;
import net.pawlrip.block.entity.StSBlockEntities;
import net.pawlrip.render.block.entity.WandMakerBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class StSBlockEntityRenderer {

    public static void registerModBlockEntityRenderers() {
        StSMain.LOGGER.info("Registering Mod Block Entity Renderers for " + StSMain.MOD_NAME);

        BlockEntityRendererFactories.register(StSBlockEntities.WANDMAKER_BLOCK_ENTITY, WandMakerBlockEntityRenderer::new);
    }
}

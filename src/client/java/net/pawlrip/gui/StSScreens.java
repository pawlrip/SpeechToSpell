package net.pawlrip.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.pawlrip.StSMain;
import net.pawlrip.screen.StSScreenHandlers;

@Environment(value=EnvType.CLIENT)
public class StSScreens {
    public static void registerModScreens() {
        HandledScreens.register(StSScreenHandlers.WANDMAKER_SCREEN_HANDLER, WandMakerScreen::new);

        StSMain.LOGGER.info("Registering Mod Screens for " + StSMain.MOD_NAME);
    }
}

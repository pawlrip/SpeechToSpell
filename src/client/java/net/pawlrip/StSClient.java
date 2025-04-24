package net.pawlrip;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.pawlrip.gui.MagicHud;
import net.pawlrip.gui.StSScreens;
import net.pawlrip.render.StSEntityRenderer;
import net.pawlrip.render.block.StSBlockEntityRenderer;
import net.pawlrip.speech.SpeechRecognizer;

@Environment(EnvType.CLIENT)
public class StSClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        StSKeybindings.registerCustomKeybindings();
        StSEntityRenderer.registerModEntityRenderers();
        StSBlockEntityRenderer.registerModBlockEntityRenderers();
        StSClientNetwork.registerNetworkPackets();
        StSScreens.registerModScreens();
        MagicHud.initialize();
        SpeechRecognizer.initialize();
    }
}

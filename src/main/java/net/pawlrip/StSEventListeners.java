package net.pawlrip;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pawlrip.cardinal_component.player_magic_comp.ServerPlayerMagicComponent;
import net.pawlrip.spell.SpellManager;
import net.pawlrip.spell_school.SpellSchoolManager;

public class StSEventListeners {
    private static boolean reloadInProgress = false;

    public static boolean isReloadInProgress() {
        return reloadInProgress;
    }

    public static void registerListeners() {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> reloadInProgress = true);

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
            assert player.getServer() != null;
            if (!player.getServer().isHost(player.getGameProfile())) {
                SpellSchoolManager.syncToClient(player);
                SpellManager.syncToClient(player);
            }

            ServerPlayerMagicComponent playerMagicComp = ServerPlayerMagicComponent.getInstance(player);
            if (!joined) {
                StSMain.LOGGER.debug("Reloading and syncing PlayerData for player {}", player);
                playerMagicComp.onDataPacksReloaded();
            }
            playerMagicComp.fullSync();
        });


        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resManager, success) -> reloadInProgress = false);


        StSMain.LOGGER.info("Registering event listeners for " + StSMain.MOD_NAME);
    }
}

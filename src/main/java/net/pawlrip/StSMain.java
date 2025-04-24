package net.pawlrip;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.pawlrip.block.StSBlocks;
import net.pawlrip.block.entity.StSBlockEntities;
import net.pawlrip.command.StSGameRules;
import net.pawlrip.entity.StSEntities;
import net.pawlrip.item.StSItems;
import net.pawlrip.network.StSNetwork;
import net.pawlrip.recipe.StSRecipes;
import net.pawlrip.screen.StSScreenHandlers;
import net.pawlrip.spell.SpellManager;
import net.pawlrip.spell.SpellTypes;
import net.pawlrip.spell.component.SpellStateComponentTypes;
import net.pawlrip.spell_school.SpellSchoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO README.md
public class StSMain implements ModInitializer {

	public static final String MOD_ID = "speech_to_spell";
	public static final String MOD_NAME = "SpeechToSpell";

	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	@Override
	public void onInitialize() {

		StSItems.registerModItems();
		StSBlocks.registerModBlocks();
		StSBlockEntities.registerModBlockEntities();
		StSScreenHandlers.registerModScreenHandlers();
		StSRecipes.registerModRecipes();
		StSNetwork.registerNetworkPackets();
		StSEntities.registerModEntities();
		StSParticles.registerModParticles();

		StSGameRules.initialize();

		SpellTypes.initialize();
		SpellStateComponentTypes.initialize();

		StSEventListeners.registerListeners();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SpellSchoolManager());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SpellManager());
	}

	/**
	 * @param path The path the returned identifier should have.
	 * @return An identifier with the namespace of the SpeechToSpell mod.
	 */
	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}

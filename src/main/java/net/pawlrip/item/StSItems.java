package net.pawlrip.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.pawlrip.StSMain;
import net.pawlrip.item.custom.GuideBookItem;
import net.pawlrip.item.custom.WandItem;
import org.jetbrains.annotations.Nullable;

public class StSItems {

    public static final Item WAND = registerItem("wand",
            new WandItem(new FabricItemSettings()));

    public static final Item CRIMSON_WAND = registerItem("crimson_wand",
            new WandItem(new FabricItemSettings()), ItemGroups.TOOLS);

    public static final Item GUIDE_BOOK = registerItem("guide_book",
            new GuideBookItem(new FabricItemSettings()), ItemGroups.TOOLS);

    public static final Item LIGHTNING_ICON_ITEM = registerDisplayIconItem("lightning");
    public static final Item ATTRACT_BLOCK_ICON_ITEM = registerDisplayIconItem("attract_block");

    private static Item registerItem(String name, Item item, @Nullable RegistryKey<ItemGroup> group) {
        Item item1 = Registry.register(Registries.ITEM, StSMain.id(name), item);
        if (group != null) ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item1));
        return item1;
    }

    private static Item registerItem(String name, Item item) {
        return registerItem(name, item, null);
    }

    /**
     * Registers an item whose purpose is only to be used for displaying an icon for a spell.
     * @see net.pawlrip.spell.spells.Spell#getDisplayItem()
     */
    public static Item registerDisplayIconItem(Identifier id) {
        return Registry.register(Registries.ITEM, id, new Item(new FabricItemSettings()));
    }

    private static Item registerDisplayIconItem(String path) {
        return registerDisplayIconItem(StSMain.id(path));
    }

    public static void registerModItems() {
        StSMain.LOGGER.info("Registering Mod Items for " + StSMain.MOD_NAME);
    }
}

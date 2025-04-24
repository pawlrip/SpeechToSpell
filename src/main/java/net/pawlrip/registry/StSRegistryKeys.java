package net.pawlrip.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.pawlrip.StSMain;
import net.pawlrip.spell.SpellTypes;
import net.pawlrip.spell.component.SpellStateComponentTypes;
import net.pawlrip.spell.spells.Spell;
import net.pawlrip.spell.target.TargetAreaTypes;
import net.pawlrip.spell.target.TargetTypes;
import net.pawlrip.spell.target.areas.TargetArea;
import net.pawlrip.spell.target.targets.Target;

public class StSRegistryKeys {
    public static final RegistryKey<Registry<SpellTypes.SpellType<? extends Spell>>> SPELL_TYPE = of("spell_type");
    public static final RegistryKey<Registry<SpellStateComponentTypes.SpellStateComponentType<?>>> SPELL_STATE_COMPONENT_TYPE = of("spell_state_component_type");
    public static final RegistryKey<Registry<TargetAreaTypes.TargetAreaType<? extends TargetArea>>> TARGET_AREA_TYPE = of("target_area_type");
    public static final RegistryKey<Registry<TargetTypes.TargetType<? extends Target>>> TARGET_TYPE = of("target_type");

    private static <T> RegistryKey<Registry<T>> of(String path) {
        return RegistryKey.ofRegistry(new Identifier(StSMain.MOD_ID, path));
    }
}

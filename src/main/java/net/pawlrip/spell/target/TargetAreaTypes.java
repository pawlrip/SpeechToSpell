package net.pawlrip.spell.target;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.pawlrip.StSMain;
import net.pawlrip.registry.StSRegistries;
import net.pawlrip.spell.target.areas.FocusedArea;
import net.pawlrip.spell.target.areas.SelfArea;
import net.pawlrip.spell.target.areas.SphereArea;
import net.pawlrip.spell.target.areas.TargetArea;

/**
 * A registry of {@link TargetArea}s.
 * <p>Spells may use the {@link #CODEC} to make their target area configurable.
 * <p><b>Not to be confused with {@link TargetTypes}.</b>
 * @see <a href="https://docs.fabricmc.net/1.20.4/develop/codecs#registry-dispatch">Fabric-Wiki Registry Dispatch</a>
 */
public class TargetAreaTypes {
    public static final Codec<TargetArea> CODEC = StSRegistries.TARGET_AREA_TYPE.getCodec()
            .dispatch("type", TargetArea::getType, TargetAreaType::codec);

    public static final TargetAreaType<SelfArea> SELF = register("self", SelfArea.CODEC);
    public static final TargetAreaType<FocusedArea> FOCUSED = register("focused", FocusedArea.CODEC);

    /**
     * This method may only be used to register the TargetArea types, the SpeechToSpell mod implements directly.
     */
    private static <T extends TargetArea> TargetAreaType<T> register(String name, Codec<T> targetAreaCodec) {
        return register(StSMain.id(name), targetAreaCodec);
    }

    /**
     * Registers a new {@link TargetArea}.
     * <p>This method may be used by other mods that want to add target areas.<br>
     * The target areas added by other mods may also be used for the built-in spells.
     * @param identifier The id of the target area.
     * @param targetAreaCodec The codec determines how a target area can be configured from json.
     * @param <T> The class of the target area.
     */
    public static <T extends TargetArea> TargetAreaType<T> register(Identifier identifier, Codec<T> targetAreaCodec) {
        return Registry.register(StSRegistries.TARGET_AREA_TYPE, identifier, new TargetAreaType<>(targetAreaCodec));
    }

    public record TargetAreaType<T extends TargetArea>(Codec<T> codec) {}
}

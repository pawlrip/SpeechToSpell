package net.pawlrip.spell.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.pawlrip.codecs.CodecUtil;
import net.pawlrip.resource.MergingJsonDataLoader;
import net.pawlrip.spell.SpellState;
import net.pawlrip.spell.SpellTypes;
import net.pawlrip.spell.component.SpellStateComponentMap;
import net.pawlrip.spell.component.SpellStateComponentTypes;

import java.util.Collections;
import java.util.Map;

/**
 * The base class for spells,
 * which can be cast by entities that have a {@link net.pawlrip.cardinal_component.MagicComponent}.
 *
 * <p>Like {@link Item} or {@link net.minecraft.block.Block},
 * this class handles the logics for a spell and does not hold any data apart from its initial configuration.<br>
 * The dynamic data of a spell (e.g. the cooldown), in regard to a specific entity, is held by a {@link SpellState}.
 * Every magical entity stores a state for each spell in its {@link net.pawlrip.cardinal_component.MagicComponent}.
 *
 * <p>Spells are loaded and contained by the {@link net.pawlrip.spell.SpellManager}.<br>
 * There might be multiple instances of the same Spell-Class in the SpellManager, but every loaded spell
 * will have a unique id and incantation.
 *
 * <p>A spell should not be confused with its {@link SpellTypes.SpellType},
 * which is used to determine which Spell-Class should be instantiated when loading the spell from json.
 * Unlike spells, SpellTypes are unique.<br>
 * For each Spell-Class there must be one registered SpellType, which the spell returns with {@link #getType()}.
 *
 * <p>View the {@link net.pawlrip.spell.spells} directory
 * to see how some implementations of Spell-Classes might look like.
 *
 * @see SpellTypes
 * @see net.pawlrip.spell.SpellManager
 * @see SpellState
 * @see net.pawlrip.cardinal_component.MagicComponent
 * @see net.pawlrip.cardinal_component.player_magic_comp.PlayerMagicComponent
 */
public interface Spell {

    /**
     * Every spell needs to create a {@link SpellTypes.SpellType} entry in {@link SpellTypes}.
     * <p> The SpellType entry is used to determine which Spell-Class should be used when loading a spell from json.
     * <p>The id of the SpellType entry is not to be confused
     * with the id, which the loaded spell will later have in its BaseConfiguration.
     * @return The corresponding entry from {@link SpellTypes}.
     */
    SpellTypes.SpellType<? extends Spell> getType();

    /**
     * @return The basic configuration of the spell.
     */
    BaseConfiguration baseConf();

    /**
     * This check is not reached if there are already reasons for the spell not to be cast (e.g. it's cooling down).
     * @param state The state of the spell.
     * @return {@code true} when the spell can be cast.
     */
    default boolean canCast(SpellState state) {
        return true;
    }

    /**
     * Controls the main logic of the spell.
     *
     * <p> Is called as long as the spell is actively being cast.
     * The time for which the spell is cast is controlled in the spells state {@link SpellState#tickCasting()}.
     *
     * <p> If the casting should finish early,
     * <strong>{@link #endEarly(SpellState)}<u> is automatically called</u></strong>,
     * thus logic for cleaning up should be put in the endEarly method.
     *
     * @param state The state of the spell.
     * @return {@code true} when casting should finish early.
     */
    boolean tickCasting(SpellState state);

    /**
     * Is automatically called if the spell is stopped early during casting.
     *
     * <p>E.g. is called when an entity changes the spell it is currently casting.
     *
     * @param state The state of the spell.
     */
    default void endEarly(SpellState state) {}

    /**
     * Is called every tick the spell is cooling down.
     *
     * <p>Normally there is no need for spells to implement this method.
     *
     * <p>The actual logic for reducing the cooldown time is handled by {@link SpellState#cooldownTick()}.
     *
     * @param state The state of the spell.
     *
     * @return {@code true} when the cooldown should finish early.
     */
    default boolean tickCooldown(SpellState state) {
        return false;
    }

    /**
     * By overriding this method, spells can ensure that their state has the specified components at all times.
     *
     * <p><strong>Each pair of {@code ?} and {@code Object} must be of the same type, otherwise errors will arise!</strong>
     *
     * @return The {@link SpellStateComponentTypes.SpellStateComponentType}s that this spell requires<br>
     * (e.g. to work every tick) and their corresponding default values.
     */
    default Map<SpellStateComponentTypes.SpellStateComponentType<?>, Object> getRequiredSpellStateComponents() {
        return Collections.emptyMap();
    }

    /**
     * @param entity The entity the state should belong to.
     * @return The default state of the spell.
     */
    default SpellState getDefaultState(LivingEntity entity) {
        return new SpellState(entity, this,
                this.baseConf().defaultUnlockState(),
                new SpellStateComponentMap(this.getRequiredSpellStateComponents())
        );
    }

    /**
     * @return The id with which the spell is registered in the {@link net.pawlrip.spell.SpellManager}.
     */
    default Identifier getId() {
        return this.baseConf().id();
    }

    /**
     * @return The word(s) that the speech recognition needs to recognize to cast the spell.
     */
    default String getIncantation() {
        return this.baseConf().incantation();
    }

    /**
     * @return The name of the spell (Text formatting and translations can be used in the json files).
     */
    default Text getName() {
        return this.baseConf().name();
    }

    /**
     * @return The description of the spell (Text formatting and translations can be used in the json files).
     */
    default Text getDescription() {
        return this.baseConf().description();
    }

    /**
     * @return The spells icon.
     */
    default Icon getIcon() {
        return this.baseConf().icon();
    }

    /**
     * @return In what way the spell is unlocked by default<br>(e.g. it's locked, unlocked, speechless casting is unlocked, etc.).
     */
    default SpellState.UnlockState getDefaultUnlockState() {
        return this.baseConf().defaultUnlockState();
    }

    /**
     * @return The ticks it takes to cool this spell down.
     */
    default int getCooldownTime() {
        return this.baseConf().cooldown();
    }

    /**
     * @return The time in ticks for which the spell is cast.
     */
    default int getCastingTime() {
        return this.baseConf().duration();
    }

    /**
     * Contains the basic configuration options a spell needs to have and be able to provide.<br>
     * It is strongly advised that spells use the {@code BaseConfiguration.MAP_CODEC} for their own codec:
     * <pre> {@code
     *     public static final Codec<ExampleSpell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
     *             BaseConfiguration.MAP_CODEC.forGetter(ExampleSpell::baseConf),
     *             // Add spell specific configuration options here, e.g.:
     *             Codec.INT.optionalFieldOf("example_int", 0).forGetter(SomeSpell::exampleInt)
     *     ).apply(instance, ExampleSpell::new));
     * }</pre>
     *
     * @param id See {@link #getId()}
     * @param incantation See {@link #getIncantation()}
     * @param name See {@link #getName()}
     * @param description See {@link #getDescription()}
     * @param icon See {@link #getIcon()}
     * @param defaultUnlockState See {@link #getDefaultUnlockState}
     * @param duration See {@link #getCastingTime()}
     * @param cooldown See {@link #getCooldownTime()}
     */
    record BaseConfiguration(
            Identifier id,
            String incantation,
            Text name,
            Text description,
            Icon icon,
            SpellState.UnlockState defaultUnlockState,
            int duration,
            int cooldown
    ) {
        public static final MapCodec<BaseConfiguration> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf(MergingJsonDataLoader.ID_FIELD).forGetter(BaseConfiguration::id),
                Codec.STRING.fieldOf("incantation").forGetter(BaseConfiguration::incantation),
                CodecUtil.BETTER_TEXT.fieldOf("name").forGetter(BaseConfiguration::name),
                CodecUtil.BETTER_TEXT.optionalFieldOf("description", Text.empty()).forGetter(BaseConfiguration::description),
                CodecUtil.ICON.optionalFieldOf("icon", new TextureIcon(new Identifier("missingno"))).forGetter(BaseConfiguration::icon),
                SpellState.UnlockState.CODEC.fieldOf("default_unlock_state").forGetter(BaseConfiguration::defaultUnlockState),
                Codecs.NONNEGATIVE_INT.fieldOf("duration").forGetter(BaseConfiguration::duration),
                Codecs.NONNEGATIVE_INT.fieldOf("cooldown").forGetter(BaseConfiguration::cooldown)
        ).apply(instance, BaseConfiguration::new));
    }
}

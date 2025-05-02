package net.pawlrip.spell.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Box;
import net.pawlrip.spell.SpellState;
import net.pawlrip.spell.SpellTypes;

import java.util.function.Predicate;

/**
 * Lets the caster collect things ({@link #collect_items}, {@link #collect_xp}, {@link #collect_projectiles}),
 * laying in the {@link #range}.
 * <p>At the moment this spell only has an effect when a player casts it.
 */
public record CollectSpell(
        BaseConfiguration baseConf,
        int range,
        boolean collect_items,
        boolean collect_xp,
        boolean collect_projectiles
) implements Spell {

    public static final Codec<CollectSpell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BaseConfiguration.MAP_CODEC.forGetter(CollectSpell::baseConf),
            Codecs.NONNEGATIVE_INT.fieldOf("range").forGetter(CollectSpell::range),
            Codec.BOOL.fieldOf("collect_items").forGetter(CollectSpell::collect_items),
            Codec.BOOL.fieldOf("collect_xp").forGetter(CollectSpell::collect_xp),
            Codec.BOOL.fieldOf("collect_projectiles").forGetter(CollectSpell::collect_projectiles)
    ).apply(instance, CollectSpell::new));

    @Override
    public SpellTypes.SpellType<? extends Spell> getType() {
        return SpellTypes.COLLECT;
    }

    @Override
    public boolean tickCasting(SpellState state) {
        LivingEntity caster = state.getEntity();
        Box box = caster.getBoundingBox().expand(this.range);

        Predicate<Entity> canPickup = e -> false;
        if (this.collect_items) canPickup = e -> e instanceof ItemEntity;
        if (this.collect_xp) canPickup = canPickup.or(e -> e instanceof ExperienceOrbEntity);
        if (this.collect_projectiles) canPickup = canPickup.or(e -> e instanceof ProjectileEntity);

        for (Entity entityInRange : caster.getWorld().getOtherEntities(null, box, canPickup)) {
            if (caster instanceof PlayerEntity) entityInRange.onPlayerCollision((PlayerEntity) caster);
            // TODO allow for non-player casters to pick up items.
        }
        return false;
    }
}

package net.pawlrip.spell.target.targets.entity;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.pawlrip.spell.target.TargetTypes;
import net.pawlrip.spell.target.targets.Target;

import java.util.function.Predicate;

public record PlayerTarget(boolean includeCaster) implements Target.EntityTarget {
    public static final Codec<PlayerTarget> CODEC = Codec.BOOL
            .optionalFieldOf("include_caster", true)
            .xmap(PlayerTarget::new, t -> t.includeCaster).codec();

    @Override
    public TargetTypes.TargetType<? extends Target> getType() {
        return TargetTypes.PLAYER;
    }

    @Override
    public Predicate<Entity> getEntityPredicate(LivingEntity caster) {
        Predicate<Entity> predicate = e -> e instanceof PlayerEntity;
        return this.includeCaster ? predicate : predicate.and(e -> e != caster);
    }
}

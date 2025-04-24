package net.pawlrip.spell.target.targets.entity;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.pawlrip.spell.target.TargetTypes;
import net.pawlrip.spell.target.targets.Target;

import java.util.function.Predicate;

public record LivingEntityTarget(boolean includeCaster) implements Target.EntityTarget {
    public static final Codec<LivingEntityTarget> CODEC = Codec.BOOL
            .optionalFieldOf("include_caster", true)
            .xmap(LivingEntityTarget::new, t -> t.includeCaster).codec();

    @Override
    public TargetTypes.TargetType<? extends Target> getType() {
        return TargetTypes.LIVING_ENTITY;
    }

    @Override
    public Predicate<Entity> getEntityPredicate(LivingEntity caster) {
        Predicate<Entity> predicate = e -> e instanceof LivingEntity;
        return this.includeCaster ? predicate : predicate.and(e -> e != caster);
    }
}

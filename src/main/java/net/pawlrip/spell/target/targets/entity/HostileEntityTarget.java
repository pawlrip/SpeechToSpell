package net.pawlrip.spell.target.targets.entity;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.pawlrip.spell.target.TargetTypes;
import net.pawlrip.spell.target.targets.Target;

import java.util.function.Predicate;

public class HostileEntityTarget implements Target.EntityTarget {
    public static final Codec<HostileEntityTarget> CODEC = Codec.unit(HostileEntityTarget::new);

    @Override
    public TargetTypes.TargetType<? extends Target> getType() {
        return TargetTypes.HOSTILE_ENTITY;
    }

    @Override
    public Predicate<Entity> getEntityPredicate(LivingEntity caster) {
        return e -> e instanceof HostileEntity;
    }
}

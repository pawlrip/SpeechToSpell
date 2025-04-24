package net.pawlrip.spell.target.targets.block;

import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.pawlrip.spell.target.TargetTypes;
import net.pawlrip.spell.target.targets.Target;

import java.util.function.Predicate;

public class AnyBlockTarget implements Target.BlockTarget {
    public static final Codec<AnyBlockTarget> CODEC = Codec.unit(AnyBlockTarget::new);

    @Override
    public TargetTypes.TargetType<? extends Target> getType() {
        return TargetTypes.BLOCK;
    }

    @Override
    public Predicate<BlockPos> getBlockPredicate(LivingEntity caster) {
        return e -> true;
    }
}

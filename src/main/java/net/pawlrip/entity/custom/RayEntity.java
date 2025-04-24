package net.pawlrip.entity.custom;


import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.pawlrip.StSParticles;
import net.pawlrip.spell.spells.Spell;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RayEntity extends ExplosiveProjectileEntity {

    private boolean burning = false;
    private ParticleEffect particleType = StSParticles.EMPTY_PARTICLE;
    private float drag = 1; // set to 1 for no drag
    private boolean canHit = false;
    private int maxLifetime = 1000;
    private Spell spell;
    private String spellName;
    private int minProtegoType = 1; // if zero no protego spell is strong enough to protect against this spell

    public RayEntity(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
        super.setNoGravity(true);
    }

    public void configureEntity(@Nullable Entity owner, int maxLifetime, Spell spell, String name) {
        this.setOwner(owner);
        this.setMaxLifetime(maxLifetime);
        this.setSpell(spell);
        this.setSpellName(name);
    }

    public void updatePositionAndAngles(LivingEntity player) {
        this.updatePositionAndAngles(player.getX(), player.getEyeY(), player.getZ(), player.getYaw() + 180, player.getPitch() * -1);
    }

    public void setVelocity(LivingEntity entity, float speed, float divergence) {
        float yaw = entity.getYaw();
        float pitch = entity.getPitch();
        float roll = entity.getRoll();

        // pi / 180 = 0.017453292F
        float x = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float y = -MathHelper.sin((pitch + roll) * 0.017453292F);
        float z = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);

        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(this.random.nextGaussian() * 0.007499999832361937 * (double)divergence, this.random.nextGaussian() * 0.007499999832361937 * (double)divergence, this.random.nextGaussian() * 0.007499999832361937 * (double)divergence).multiply(speed);
        this.setVelocity(vec3d);
    }

    @Override
    public void setVelocityClient(double x, double y, double z) {
        this.setVelocity(x, y, z);
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            this.prevPitch = this.getPitch();
            this.prevYaw = this.getYaw();
            this.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.tick2();

        ++this.age;
        if (this.age >= this.maxLifetime) {
            this.kill();
        }
    }

    public void tick2() {
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        this.writeCustomDataToNbt2(nbt);
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("burning", this.burning);
        nbt.putFloat("drag", this.drag);
        nbt.putBoolean("canHit", this.canHit);
        nbt.putInt("maxLifetime", this.maxLifetime);
        nbt.putString("spellName", this.spellName);
        nbt.putInt("minProtegoType", this.minProtegoType);

        if (this.spell == null) {
            this.discard();
        } else {
            nbt.putString("spell", this.spell.toString());
        }
        if (this.particleType == null) {
            this.discard();
        } else {
            nbt.putString("particleType", this.particleType.asString());
        }
    }

    public void writeCustomDataToNbt2(NbtCompound nbtCompound) {
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.readCustomDataFromNbt2(nbt);
        super.readCustomDataFromNbt(nbt);
        this.burning = nbt.getBoolean("burning");
        this.drag = nbt.getFloat("drag");
        this.canHit = nbt.getBoolean("canHit");
        this.maxLifetime = nbt.getInt("maxLifetime");
        this.spellName = nbt.getString("spellName");
        this.minProtegoType = nbt.getInt("minProtegoType");
        if (!Objects.equals(nbt.getString("spell"), "")) {
            /*this.spell = SpellType.valueOf(nbt.getString("spell"));*/
        }
        if (!Objects.equals(nbt.getString("particleType"), "")) {
            try {
                this.particleType = ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("particleType")), Registries.PARTICLE_TYPE.getReadOnlyWrapper());
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readCustomDataFromNbt2(NbtCompound nbt){
    }

    @Override
    protected boolean isBurning() {
        return this.burning;
    }

    public void setBurning(boolean bl) {
        this.burning = bl;
    }

    @Override
    protected ParticleEffect getParticleType() {
        return this.particleType;
    }

    public void setParticeType(ParticleEffect particeType) {
        this.particleType = particeType;
    }

    @Override
    protected float getDrag() {
        return this.drag;
    }

    public void setDrag(float drag) {
        this.drag = drag;
    }

    @Override
    public boolean canHit() {
        return this.canHit;
    }

    public void setCanHit(boolean canHit) {
        this.canHit = canHit;
    }

    public int getMaxLifetime() {
        return this.maxLifetime;
    }

    public void setMaxLifetime(int maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public Spell getSpell() {
        return this.spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public String getSpellName() {
        return this.spellName;
    }

    public void setSpellName(String name) {
        this.spellName = name;
    }

    public int getMinProtegoType() {
        return this.minProtegoType;
    }

    public void setMinProtegoType(int minProtegoType) {
        this.minProtegoType = minProtegoType;
    }
}

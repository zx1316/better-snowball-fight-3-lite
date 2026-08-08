package com.linngdu664.bsf3lite.entity.snowball;

import com.linngdu664.bsf3lite.config.ServerConfig;
import com.linngdu664.bsf3lite.entity.golem.AbstractBSFSnowGolemEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.entity.snowball.util.LaunchFrom;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.item.tool.GloveItem;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.registry.ParticleRegistry;
import com.linngdu664.bsf3lite.registry.TriggerTypeRegistry;
import com.linngdu664.bsf3lite.util.BSFCommonUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBSFSnowballEntity extends ThrowableItemProjectile {
    protected float particleGenerationStepSize = 0.5F;
    protected float particleGeneratePointOffset = 0;
    protected Vec3 previousTickPosition = new Vec3(Double.NaN, Double.NaN, Double.NaN);
    protected boolean isCaught = false;
    private float damage;
    private float blazeDamage;
    private int weaknessTicks;
    private int frozenTicks;
    private float punch;
    private boolean canBeCaught;
    private LaunchFrom launchFrom;
    private RegionData aliveRange;

    public AbstractBSFSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel, BSFSnowballEntityProperties pProperties) {
        super(pEntityType, pLevel);
        copyProperties(pProperties);
    }

    public AbstractBSFSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel, ItemStack itemStack, BSFSnowballEntityProperties pProperties) {
        super(pEntityType, pX, pY, pZ, pLevel, itemStack);
        copyProperties(pProperties);
    }

    public AbstractBSFSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, LivingEntity pShooter, Level pLevel, ItemStack itemStack, BSFSnowballEntityProperties pProperties) {
        super(pEntityType, pShooter, pLevel, itemStack);
        copyProperties(pProperties);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("Damage", damage);
        output.putFloat("BlazeDamage", blazeDamage);
        output.putInt("WeaknessTicks", weaknessTicks);
        output.putInt("FrozenTicks", frozenTicks);
        output.putFloat("Punch", punch);
        output.putBoolean("CanBeCaught", canBeCaught);
        output.putInt("LaunchFrom", launchFrom.ordinal());
        output.putFloat("ParticleGenerationStepSize", particleGenerationStepSize);
        output.putFloat("ParticleGenerationPointOffset", particleGeneratePointOffset);
        if (aliveRange != null) {
            aliveRange.saveToValueOutput("AliveRange", output);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        damage = input.getFloatOr("Damage", 0f);
        blazeDamage = input.getFloatOr("BlazeDamage", 0);
        weaknessTicks = input.getIntOr("WeaknessTicks", 0);
        frozenTicks = input.getIntOr("FrozenTicks", 0);
        punch = input.getFloatOr("Punch", 0);
        canBeCaught = input.getBooleanOr("CanBeCaught", false);

        int launchFromOrdinal = input.getIntOr("LaunchFrom", 0);
        LaunchFrom[] launchFroms = LaunchFrom.values();
        launchFrom = launchFromOrdinal >= 0 && launchFromOrdinal < launchFroms.length ? launchFroms[launchFromOrdinal] : LaunchFrom.HAND;

        particleGenerationStepSize = input.getFloatOr("ParticleGenerationStepSize", 0.5f); // command summoned fallback
        particleGeneratePointOffset = input.getFloatOr("ParticleGenerationPointOffset", 0);
        aliveRange = RegionData.loadFromValueInput("AliveRange", input);
    }

    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -Mth.sin(y * 0.017453292F) * Mth.cos(x * 0.017453292F);
        float f1 = -Mth.sin((x + z) * 0.017453292F);
        float f2 = Mth.cos(y * 0.017453292F) * Mth.cos(x * 0.017453292F);
        this.shoot(f, f1, f2, velocity, inaccuracy);
        Vec3 vec3 = ServerConfig.SHOOTING_INERTIA.getConfigValue() ? shooter.getKnownMovement() : new Vec3(0,shooter.getDeltaMovement().y,0);
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, shooter.onGround() ? 0.0 : vec3.y, vec3.z));
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Level level = level();
        if (pResult.getEntity() instanceof LivingEntity entity) {
            // Handling the catch
            if (catchOnGlove(entity)) {
                if (!level.isClientSide()) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 3, 0, 0, 0, 0.04);
                }
                isCaught = true;
                return;
            }

            Vec3 vel = getDeltaMovement();

            // Damage entity
            float hurt = entity instanceof Blaze ? blazeDamage : damage;
            float relVel = (float) vel.subtract(entity.getDeltaMovement()).length();
            hurt *= 0.5f + 0.375f * relVel;
            if (entity.hurtOrSimulate(this.damageSources().thrown(this, this.getOwner()), hurt)) {
                // Handle frozen and weakness effects
                if (frozenTicks > 0 && !(entity instanceof AbstractBSFSnowGolemEntity) && !(entity instanceof SnowGolem)) {
                    if (entity.getTicksFrozen() < frozenTicks) {
                        entity.setTicksFrozen(frozenTicks);
                    }
                    entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20, 1));
                }
                if (weaknessTicks > 0) {
                    entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, weaknessTicks, 1));
                }

                // Push entity
                if (entity.isPushable()) {
                    Vec3 vec3d = vel.multiply(0.1 * punch, 0.0, 0.1 * punch);
                    entity.push(vec3d.x, 0.0, vec3d.z);
                }
                if (getOwner() instanceof LivingEntity owner) {
                    owner.setLastHurtMob(entity);
                    if (owner instanceof ServerPlayer serverPlayer) {
                        TriggerTypeRegistry.SNOWBALL_DAMAGE_TRIGGER.get().trigger(serverPlayer, this, hurt);
                    }
                }
            }
        }
        Vec3 location = BSFCommonUtil.getRealEntityHitPosOnMoveVecWithHitResult(this, pResult);
        spawnBasicParticles(level, location);
        callTraceParticlesEnd(location);
    }

    /**
     * Triggered when an entity hits a block.
     *
     * @param p_37258_ blockHitResult
     */
    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        Vec3 location = p_37258_.getLocation();
        spawnBasicParticles(level(), location);
        callTraceParticlesEnd(location);
    }

    /**
     * This method will be called every tick.
     */
    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide() && aliveRange != null && !aliveRange.inRegion(position())) {
            discard();
        }
        callTraceParticles();
    }

    protected void callTraceParticles() {
        if (!Double.isNaN(previousTickPosition.x)) {
            float v = (float) this.getDeltaMovement().length();
            int n = (int) (v / particleGenerationStepSize);
            int num = 0;
            for (int i = 0; i <= n && particleGeneratePointOffset + i * particleGenerationStepSize < v; i++) {
                generateVelIndependentTraceParticles(this.getPreviousPosition((particleGeneratePointOffset + i * particleGenerationStepSize) / v, previousTickPosition));
                num++;
            }
            particleGeneratePointOffset = num * particleGenerationStepSize + particleGeneratePointOffset - v;
        }
        previousTickPosition = this.getPosition(0);
    }

    protected void callTraceParticlesEnd(Vec3 pos) {
        float v = (float) this.getPosition(1).distanceTo(pos);
        Vec3 vec3d = this.getPosition(1).add(this.getDeltaMovement().normalize().scale(v));
        int n = (int) (v / particleGenerationStepSize);
        for (int i = 0; i <= n && particleGeneratePointOffset + i * particleGenerationStepSize < v; i++) {
            generateVelIndependentTraceParticles(this.getCurrentlyPosition((particleGeneratePointOffset + i * particleGenerationStepSize) / v, vec3d));
        }
    }

    protected void generateVelIndependentTraceParticles(Vec3 vec3) {
        // Spawn trace particles
        Level level = level();
        if (level.isClientSide()) {
            level.addParticle(ParticleRegistry.SHORT_TIME_SNOWFLAKE.get(), vec3.x, vec3.y + 0.1, vec3.z, 0, 0, 0);
        }
    }

    public final Vec3 getPreviousPosition(float pPartialTicks, Vec3 previousTickPosition) {
        double d0 = Mth.lerp(pPartialTicks, previousTickPosition.x, this.xo);
        double d1 = Mth.lerp(pPartialTicks, previousTickPosition.y, this.yo);
        double d2 = Mth.lerp(pPartialTicks, previousTickPosition.z, this.zo);
        return new Vec3(d0, d1, d2);
    }

    public final Vec3 getCurrentlyPosition(float pPartialTicks, Vec3 position) {
        double d0 = Mth.lerp(pPartialTicks, this.xo, position.x);
        double d1 = Mth.lerp(pPartialTicks, this.yo, position.y);
        double d2 = Mth.lerp(pPartialTicks, this.zo, position.z);
        return new Vec3(d0, d1, d2);
    }

    /**
     * @param entity The player who is using the glove.
     * @return If the glove catches return true.
     */
    private boolean catchOnGlove(LivingEntity entity) {
        Level level = level();
        if (entity instanceof Player player) {
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();
            if ((offHand.getItem() instanceof GloveItem && player.getUsedItemHand() == InteractionHand.OFF_HAND ||
                    mainHand.getItem() instanceof GloveItem && player.getUsedItemHand() == InteractionHand.MAIN_HAND) &&
                    player.isUsingItem() && isHeadingToSnowball(player) && canBeCaught()) {
                if (mainHand.getItem() instanceof GloveItem glove) {
                    mainHand.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    glove.releaseUsing(mainHand, level, player, 1);
                } else if (offHand.getItem() instanceof GloveItem glove) {
                    offHand.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
                    glove.releaseUsing(offHand, level, player, 1);
                }
                if (!level.isClientSide()) {
                    ItemStack stack = new ItemStack(getDefaultItem());
                    if (aliveRange != null) {
                        stack.set(DataComponentRegistry.REGION, aliveRange);
                    }
                    player.getInventory().placeItemBackInInventory(stack);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL, 3F, 0.4F / level.getRandom().nextFloat() * 0.4F + 0.8F);
                    ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 3, 0, 0, 0, 0.04);
                }
                return true;
            }
        }
        return false;
    }

    // Check whether the player can catch the snowball
    private boolean isHeadingToSnowball(Player player) {
        Vec3 speedVec = this.getDeltaMovement().normalize();
        Vec3 cameraVec = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
        return Math.abs(cameraVec.dot(speedVec) + 1.0) < 0.2;
    }

    protected void handleExplosion(float radius, Vec3 location) {
        Level level = level();
        if (!level.isClientSide()) {
            GameRules gameRules = level.getServer().getGameRules();
            if (gameRules.get(GameRules.MOB_GRIEFING) && ServerConfig.EXPLOSIVE_DESTROY.getConfigValue()) {
                level.explode(getOwner(), location.x, location.y, location.z, radius, Level.ExplosionInteraction.TNT);
            } else {
                level.explode(getOwner(), location.x, location.y, location.z, radius, Level.ExplosionInteraction.NONE);
            }
        }

    }

    protected void spawnBasicParticles(Level level, Vec3 location) {
        if (!level.isClientSide()) {
            ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, location.x, location.y, location.z, 8, 0, 0, 0, 0);
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, location.x, location.y, location.z, 8, 0, 0, 0, 0.04);
        }
    }

    public final RegionData getRegion() {
        return aliveRange;
    }

    public boolean canBeCaught() {
        return this.canBeCaught;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getBlazeDamage() {
        return this.blazeDamage;
    }

    public void setBlazeDamage(float damage) {
        this.blazeDamage = damage;
    }

    public int getWeaknessTicks() {
        return this.weaknessTicks;
    }

    public int getFrozenTicks() {
        return this.frozenTicks;
    }

    public double getPunch() {
        return this.punch;
    }

    public LaunchFrom getLaunchFrom() {
        return this.launchFrom;
    }

    private void copyProperties(BSFSnowballEntityProperties properties) {
        this.damage = properties.damage;
        this.blazeDamage = properties.blazeDamage;
        this.weaknessTicks = properties.weaknessTicks;
        this.frozenTicks = properties.frozenTicks;
        this.punch = properties.punch;
        this.canBeCaught = properties.canBeCaught;
        this.launchFrom = properties.launchFrom;
        this.aliveRange = RegionData.copy(properties.aliveRange);
    }

    public static class BSFSnowballEntityProperties {
        private float damage;
        private float blazeDamage;
        private int weaknessTicks;
        private int frozenTicks;
        private float punch;
        private boolean canBeCaught;
        private @NotNull LaunchFrom launchFrom;
        private @Nullable RegionData aliveRange;

        public BSFSnowballEntityProperties() {
            this.damage = Float.MIN_NORMAL;
            this.blazeDamage = 3;
            this.weaknessTicks = 0;
            this.frozenTicks = 0;
            this.punch = 0;
            this.canBeCaught = true;
            this.launchFrom = LaunchFrom.HAND;
            this.aliveRange = null;
        }

        public BSFSnowballEntityProperties damage(float damage) {
            this.damage = damage;
            return this;
        }

        public BSFSnowballEntityProperties blazeDamage(float damage) {
            this.blazeDamage = damage;
            return this;
        }

        public BSFSnowballEntityProperties weaknessTicks(int ticks) {
            this.weaknessTicks = ticks;
            return this;
        }

        public BSFSnowballEntityProperties frozenTicks(int ticks) {
            this.frozenTicks = ticks;
            return this;
        }

        public BSFSnowballEntityProperties punch(float punch) {
            this.punch = punch;
            return this;
        }

        public BSFSnowballEntityProperties canBeCaught(boolean canBeCaught) {
            this.canBeCaught = canBeCaught;
            return this;
        }

        public BSFSnowballEntityProperties aliveRange(@Nullable RegionData aliveRange) {
            this.aliveRange = aliveRange;
            return this;
        }

        public BSFSnowballEntityProperties applyAdjustment(ILaunchAdjustment adjustment) {
            this.blazeDamage = adjustment.adjustBlazeDamage(blazeDamage);
            this.damage = adjustment.adjustDamage(damage);
            this.frozenTicks = adjustment.adjustFrozenTicks(frozenTicks);
            this.weaknessTicks = adjustment.adjustWeaknessTicks(weaknessTicks);
            this.punch = adjustment.adjustPunch(punch);
            this.launchFrom = adjustment.getLaunchFrom();
            return this;
        }
    }
}

package com.linngdu664.bsf3lite.entity;

import com.linngdu664.bsf3lite.entity.golem.AbstractBSFSnowGolemEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.jetbrains.annotations.NotNull;

public class BSFDummyEntity extends LivingEntity {
    private static final EntityDataAccessor<Float> DPS = SynchedEntityData.defineId(BSFDummyEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> STYLE = SynchedEntityData.defineId(BSFDummyEntity.class, EntityDataSerializers.BYTE);
    private final float[] damages = new float[20];
    private float damage = 0F;
    private int ptr = 0;
    private int showNameTime = 0;
    private String dpsStrCache;

    public BSFDummyEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DPS, 0F);
        builder.define(STYLE, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(STYLE, input.getByteOr("Style",  (byte) 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("Style", getStyle());
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public void setDeltaMovement(Vec3 deltaMovement) {
    }

    @Override
    protected void tickHeadTurn(float yBodyRotT) {
        this.yBodyRotO = this.yRotO;
        this.yBodyRot = this.getYRot();
    }

    @Override
    public void onDamageTaken(DamageContainer damageContainer) {
        if (!damageContainer.getSource().is(DamageTypes.GENERIC_KILL)) {
            damage += damageContainer.getNewDamage();
            setHealth(Float.MAX_VALUE);
            if (!level().isClientSide()) {
                this.setCustomNameVisible(true);
                this.showNameTime = 40;
            }
        }
    }

    @Override
    public void aiStep() {
        if (this.isInterpolating()) {
            this.getInterpolation().interpolate();
        }

        if (this.lerpHeadSteps > 0) {
            this.lerpHeadRotationStep(this.lerpHeadSteps, this.lerpYHeadRot);
            --this.lerpHeadSteps;
        }

        this.equipment.tick(this);

        ProfilerFiller profiler = Profiler.get();

        profiler.push("travel");
        if (!this.level().isClientSide() || this.isLocalInstanceAuthoritative()) {
            this.applyEffectsFromBlocks();
        }
        profiler.pop();

        if (this.level() instanceof ServerLevel serverLevel) {
            profiler.push("freezing");
            if (!this.isInPowderSnow || !this.canFreeze()) {
                this.setTicksFrozen(Math.max(0, this.getTicksFrozen() - 2));
            }
            this.removeFrost();
            this.tryAddFrost();
            if (this.tickCount % 40 == 0 && this.isFullyFrozen() && this.canFreeze()) {
                this.hurtServer(serverLevel, this.damageSources().freeze(), 1.0F);
            }
            profiler.pop();
        }

        profiler.push("push");
        this.pushEntities();
        profiler.pop();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            damages[ptr++] = damage;
            if (ptr >= damages.length) {
                ptr = 0;
            }
            float lastDps = getDPS();
            float currentDps = lastDps;
            if (damage > 0F) {
                float sum = 0F;
                for (float v : damages) {
                    sum += v;
                }
                currentDps = sum;
                entityData.set(DPS, sum);
                damage = 0F;
            }

            if (currentDps != lastDps || this.dpsStrCache == null) {
                boolean dpsTooSmall = currentDps > 0.0 && currentDps < 0.01;
                boolean dpsTooBig = currentDps >= 10.0;
                boolean showNormal = !dpsTooSmall && !dpsTooBig;
                this.dpsStrCache = String.format(showNormal ? "DPS: %.2f" : "DPS: %.3g", currentDps);
            }
            setCustomName(Component.literal(this.dpsStrCache));

            if (this.showNameTime > 0) {
                this.showNameTime--;
            } else if (this.isCustomNameVisible()) {
                this.setCustomNameVisible(false);
            }
        }
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SNOWBALL) && !player.isSpectator()) {
            if (player.isSpectator()) {
                return InteractionResult.SUCCESS;
            } else if (player.level().isClientSide()) {
                return InteractionResult.SUCCESS_SERVER;
            } else {
                Level level = level();
                entityData.set(STYLE, (byte) ((getStyle() + 1) % AbstractBSFSnowGolemEntity.STYLE_NUM));
                ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY() + 1, this.getZ(), 20, 0, 0.5, 0, 0.05);
                this.playSound(SoundEvents.SNOW_PLACE, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                return InteractionResult.SUCCESS_SERVER;
            }
        } else {
            return super.interact(player, hand, location);
        }
    }

    public float getDPS() {
        return entityData.get(DPS);
    }

    public byte getStyle() {
        return entityData.get(STYLE);
    }
}

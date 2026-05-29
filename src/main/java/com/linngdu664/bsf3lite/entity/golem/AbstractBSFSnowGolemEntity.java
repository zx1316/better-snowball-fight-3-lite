package com.linngdu664.bsf3lite.entity.golem;

import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.ItemData;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.item.snowball.AbstractBSFSnowballItem;
import com.linngdu664.bsf3lite.item.tank.LargeSnowballTankItem;
import com.linngdu664.bsf3lite.item.weapon.AbstractBSFWeaponItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballShotgunItem;
import com.linngdu664.bsf3lite.network.to_client.ForwardConeParticlesPayload;
import com.linngdu664.bsf3lite.network.to_client.packed_paras.ForwardConeParticlesParas;
import com.linngdu664.bsf3lite.particle.util.BSFParticleType;
import com.linngdu664.bsf3lite.registry.*;
import com.linngdu664.bsf3lite.util.BSFCommonUtil;
import com.linngdu664.bsf3lite.misc.BSFEnchantmentHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBSFSnowGolemEntity extends PathfinderMob implements RangedAttackMob {
    public static final int STYLE_NUM = 9;
    private static final EntityDataAccessor<ItemStack> WEAPON = SynchedEntityData.defineId(AbstractBSFSnowGolemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> AMMO = SynchedEntityData.defineId(AbstractBSFSnowGolemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> WEAPON_ANG = SynchedEntityData.defineId(AbstractBSFSnowGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> STYLE = SynchedEntityData.defineId(AbstractBSFSnowGolemEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ENHANCE = SynchedEntityData.defineId(AbstractBSFSnowGolemEntity.class, EntityDataSerializers.BOOLEAN);

    // server only
    protected float launchVelocity;
    protected float launchAccuracy;
    protected double shootX;
    protected double shootY;
    protected double shootZ;
    protected boolean dropEquipment;
    protected boolean dropSnowball;
    @Nullable
    protected RegionData aliveRange;

    protected AbstractBSFSnowGolemEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        setPathfindingMalus(PathType.WATER, -1.0F);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WEAPON, ItemStack.EMPTY);
        builder.define(AMMO, ItemStack.EMPTY);
        builder.define(WEAPON_ANG, 0);
        builder.define(STYLE, (byte) 0);
        builder.define(ENHANCE, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("Weapon", ItemStack.OPTIONAL_CODEC, getWeapon());
        output.store("Ammo", ItemStack.OPTIONAL_CODEC, getAmmo());
        output.putByte("Style", getStyle());
        output.putBoolean("Enhance", getEnhance());
        output.putBoolean("DropEquipment", dropEquipment);
        output.putBoolean("DropSnowball", dropSnowball);
        if (aliveRange != null) {
            aliveRange.saveToValueOutput("AliveRange", output);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read("Weapon", ItemStack.OPTIONAL_CODEC).ifPresent(this::setWeapon);
        input.read("Ammo", ItemStack.OPTIONAL_CODEC).ifPresent(this::setAmmo);
        setWeaponAng(input.getIntOr("WeaponAng", 0));
        setStyle(input.getByteOr("Style", (byte) 0));
        setEnhance(input.getBooleanOr("Enhance", false));
        dropEquipment = input.getBooleanOr("DropEquipment", false);
        dropSnowball = input.getBooleanOr("DropSnowball", false);
        aliveRange = RegionData.loadFromValueInput("AliveRange", input);
    }

    public ItemStack getWeapon() {
        return entityData.get(WEAPON);
    }

    public void setWeapon(ItemStack itemStack) {
        entityData.set(WEAPON, itemStack);
    }

    public ItemStack getAmmo() {
        return entityData.get(AMMO);
    }

    public void setAmmo(ItemStack itemStack) {
        entityData.set(AMMO, itemStack);
    }

    public int getWeaponAng() {
        return entityData.get(WEAPON_ANG);
    }

    public void setWeaponAng(int ang) {
        entityData.set(WEAPON_ANG, ang);
    }

    public byte getStyle() {
        return entityData.get(STYLE);
    }

    public void setStyle(byte style) {
        entityData.set(STYLE, style);
    }

    public boolean getEnhance() {
        return entityData.get(ENHANCE);
    }

    public void setEnhance(boolean enhance) {
        entityData.set(ENHANCE, enhance);
    }

    public void setLaunchVelocity(float launchVelocity) {
        this.launchVelocity = launchVelocity;
    }

    public void setLaunchAccuracy(float launchAccuracy) {
        this.launchAccuracy = launchAccuracy;
    }

    public void setShootX(double shootX) {
        this.shootX = shootX;
    }

    public void setShootY(double shootY) {
        this.shootY = shootY;
    }

    public void setShootZ(double shootZ) {
        this.shootZ = shootZ;
    }

    public void setDropEquipment(boolean b) {
        this.dropEquipment = b;
    }

    public void setDropSnowball(boolean b) {
        this.dropSnowball = b;
    }

    public void setAliveRange(RegionData region) {
        aliveRange = RegionData.copy(region);
    }

    public @Nullable RegionData getAliveRange() {
        return aliveRange;
    }

    public AABB getTargetSearchArea(int searchDistance) {
        RegionData aliveRange = getAliveRange();
        if (aliveRange != null) {
            return getBoundingBox().inflate(searchDistance, searchDistance, searchDistance).intersect(aliveRange.toBoundingBox());
        }
        return getBoundingBox().inflate(searchDistance, searchDistance, searchDistance);
    }

    public Vec3 getMiddleModelForward(float partialTicks, double degreeOffset) {
        return BSFCommonUtil.radRotationToVector(1, (Mth.lerp(partialTicks, this.yBodyRotO + ((this.yHeadRotO - this.yBodyRotO) * 0.25), this.yBodyRot + ((this.yHeadRot - this.yBodyRot) * 0.25)) + 90 + degreeOffset) * Mth.DEG_TO_RAD, 0);
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity livingEntity, float v) {
        Level level = level();
        ItemStack weapon = getWeapon();
        ItemStack ammo = getAmmo();
        AbstractBSFWeaponItem weaponItem = (AbstractBSFWeaponItem) weapon.getItem();
        if (!ammo.has(DataComponentRegistry.AMMO_ITEM) || (((AbstractBSFSnowballItem) ammo.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item()).getTypeFlag() & weaponItem.getTypeFlag()) == 0) {
            return;
        }
        ILaunchAdjustment launchAdjustment = weaponItem.getLaunchAdjustment(1, ammo.getItem());
        int j = weapon.getItem() instanceof SnowballShotgunItem ? 4 : 1;
        for (int i = 0; i < j; i++) {
            if (!ammo.has(DataComponentRegistry.AMMO_ITEM)) {
                break;
            }
            AbstractBSFSnowballEntity snowball = ((AbstractBSFSnowballItem) ammo.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item()).getCorrespondingEntity(level, this, launchAdjustment, aliveRange);
            snowball.shoot(shootX, shootY, shootZ, launchVelocity, launchAccuracy);
            level.addFreshEntity(snowball);
            if (shouldConsumeAmmo()) {
                ammo.setDamageValue(ammo.getDamageValue() + 1);
                if (ammo.getDamageValue() >= ammo.getMaxDamage()) {
                    ItemStack empty = ammo.getItem() instanceof LargeSnowballTankItem ? ItemRegistry.LARGE_SNOWBALL_TANK.get().getDefaultInstance() : ItemRegistry.SNOWBALL_TANK.get().getDefaultInstance();
                    empty.setDamageValue(empty.getMaxDamage());
                    setAmmo(empty);
                } else {
                    setAmmo(ammo.copy());   // force sync
                }
            }
            if (i == 0) {
                int aStep = 90;
                if (weaponItem.equals(ItemRegistry.POWERFUL_SNOWBALL_CANNON.get()) || weaponItem.equals(ItemRegistry.SNOWBALL_SHOTGUN.get())) {
                    aStep = 45;
                }
                PacketDistributor.sendToPlayersTrackingEntity(this, new ForwardConeParticlesPayload(new ForwardConeParticlesParas(getEyePosition(), new Vec3(shootX, shootY, shootZ), 4.5F, aStep, 1.5F, 0.1F), BSFParticleType.SNOWFLAKE.ordinal()));
                playSound(j == 4 ? SoundRegistry.SHOTGUN_FIRE_2.get() : SoundRegistry.SNOWBALL_CANNON_SHOOT.get(), 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                if (shouldDamageWeapon()) {
                    weapon.setDamageValue(weapon.getDamageValue() + 1);
                    if (weapon.getDamageValue() >= weapon.getMaxDamage()) {
                        setWeapon(ItemStack.EMPTY);
                        playSound(SoundEvents.ITEM_BREAK.value(), 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
                    } else {
                        setWeapon(weapon.copy());   // force sync
                    }
                }
                setWeaponAng(360);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Level level = level();
        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            if (level.environmentAttributes().getValue(EnvironmentAttributes.SNOW_GOLEM_MELTS, this.position())) {
                this.hurtServer(serverLevel, this.damageSources().onFire(), 1.0F);
            }
            if (!EventHooks.canEntityGrief(serverLevel, this)) {
                return;
            }
            BlockState blockState = Blocks.SNOW.defaultBlockState();
            for (int i = 0; i < 4; ++i) {
                int j = Mth.floor(getX() + (double) ((float) (i % 2 * 2 - 1) * 0.25F));
                int k = Mth.floor(getY());
                int l = Mth.floor(getZ() + (double) ((float) (i / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockPos1 = new BlockPos(j, k, l);
                if (level.isEmptyBlock(blockPos1) && blockState.canSurvive(level, blockPos1)) {
                    level.setBlockAndUpdate(blockPos1, blockState);
                    level.gameEvent(GameEvent.BLOCK_PLACE, blockPos1, GameEvent.Context.of(this, blockState));
                }
            }
        }
    }

    @Override
    public void tick() {
        Level level = level();
        if (!level.isClientSide()) {
            setTicksFrozen(0);
            if (getWeaponAng() > 0) {
                setWeaponAng(getWeaponAng() - 60);
            }
            if (getEnhance()) {
                heal(1);
                addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 2, 3));
            }
            if (aliveRange != null) {
                if (getTarget() != null && !aliveRange.inRegion(getTarget().position())) {
                    setTarget(null);
                }
                if (!aliveRange.inRegion(position()) && isAlive()) {
                    hurt(level.damageSources().genericKill(), Float.MAX_VALUE);
                }
            }
        }
        super.tick();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource damageSource, boolean recentlyHit) {
        // 永远不会掉隐藏的护甲了，同时已经判断掉落gamerule了
        if (dropEquipment) {
            int weaponVanish = EnchantmentHelper.getTagEnchantmentLevel(BSFEnchantmentHelper.getEnchantmentHolder(this, Enchantments.VANISHING_CURSE), getWeapon());
            int ammoVanish = EnchantmentHelper.getTagEnchantmentLevel(BSFEnchantmentHelper.getEnchantmentHolder(this, Enchantments.VANISHING_CURSE), getAmmo());
            int snowGolemExclusive = EnchantmentHelper.getTagEnchantmentLevel(BSFEnchantmentHelper.getEnchantmentHolder(this, BSFEnchantmentHelper.SNOW_GOLEM_EXCLUSIVE), getWeapon());
            if (weaponVanish <= 0 && snowGolemExclusive <= 0) {
                spawnAtLocation(level, getWeapon());
            }
            if (ammoVanish <= 0) {
                spawnAtLocation(level, getAmmo());
            }
        }
        if (dropSnowball) {
            spawnAtLocation(level, new ItemStack(Items.SNOWBALL, getRandom().nextInt(0, 16)));
        }
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    protected int calculateFallDamage(double fallDistance, float damageModifier) {
        return 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SNOW_GOLEM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.SNOW_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SNOW_GOLEM_DEATH;
    }

    @Override
    protected float getBlockSpeedFactor() {
        if (level().getBlockState(new BlockPos(BSFCommonUtil.vec3ToI(getPosition(0)))).getBlock().equals(BlockRegistry.CRITICAL_SNOW.get())) {
            return 1.0F;
        }
        return super.getBlockSpeedFactor();
    }

    @Override
    protected float getBlockJumpFactor() {
        if (level().getBlockState(new BlockPos(BSFCommonUtil.vec3ToI(getPosition(0)))).getBlock().equals(BlockRegistry.CRITICAL_SNOW.get())) {
            return 1.0F;
        }
        return super.getBlockJumpFactor();
    }

    public abstract boolean shouldConsumeAmmo();

    public abstract boolean shouldDamageWeapon();

    public abstract boolean canMoveAndAttack();
}

package com.linngdu664.bsf3lite.entity.golem;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.entity.ai.goal.*;
import com.linngdu664.bsf3lite.entity.ai.goal.target.BSFGolemHurtByTargetGoal;
import com.linngdu664.bsf3lite.entity.ai.goal.target.BSFGolemNearsetAttackableTargetGoal;
import com.linngdu664.bsf3lite.entity.ai.goal.target.BSFGolemOwnerHurtByTargetGoal;
import com.linngdu664.bsf3lite.entity.ai.goal.target.BSFGolemOwnerHurtTargetGoal;
import com.linngdu664.bsf3lite.item.tank.SnowballTankItem;
import com.linngdu664.bsf3lite.item.tool.SnowballClampItem;
import com.linngdu664.bsf3lite.item.weapon.cannon.SnowballCannonItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballShotgunItem;
import com.linngdu664.bsf3lite.network.to_client.ForwardRaysParticlesPayload;
import com.linngdu664.bsf3lite.network.to_client.packed_paras.ForwardRaysParticlesParas;
import com.linngdu664.bsf3lite.particle.util.BSFParticleType;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.registry.ItemRegistry;
import com.linngdu664.bsf3lite.misc.BSFEnchantmentHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class BSFSnowGolemEntity extends AbstractBSFSnowGolemEntity implements OwnableEntity {
    private static final EntityDataAccessor<Byte> STATUS_FLAG = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> LOCATOR_FLAG = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> POTION_SICKNESS = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<Component>> TARGET_NAME = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

    public BSFSnowGolemEntity(EntityType<? extends AbstractBSFSnowGolemEntity> entityType, Level level) {
        super(entityType, level);
        setDropEquipment(true);
        setDropSnowball(true);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATUS_FLAG, (byte) 0);
        builder.define(LOCATOR_FLAG, (byte) 0);
        builder.define(POTION_SICKNESS, 0);
        builder.define(TARGET_NAME, Optional.empty());
        builder.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("Status", getStatus());
        output.putByte("Locator", getLocator());
        output.putInt("PotionSickness", getPotionSickness());
        EntityReference<LivingEntity> owner = this.getOwnerReference();
        EntityReference.store(owner, output, "Owner");
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setStatus(input.getByteOr("Status", (byte) 0));
        setLocator(input.getByteOr("Locator", (byte) 0));
        setPotionSickness(input.getIntOr("PotionSickness", 0));
        EntityReference<LivingEntity> owner = EntityReference.readWithOldOwnerConversion(input, "Owner", level());
        setOwnerReference(owner);
    }

    public byte getStatus() {
        return entityData.get(STATUS_FLAG);
    }

    public void setStatus(byte status) {
        entityData.set(STATUS_FLAG, status);
    }

    public byte getLocator() {
        return entityData.get(LOCATOR_FLAG);
    }

    public void setLocator(byte locator) {
        entityData.set(LOCATOR_FLAG, locator);
    }

    public Optional<Component> getTargetName() {
        return entityData.get(TARGET_NAME);
    }

    public int getPotionSickness() {
        return entityData.get(POTION_SICKNESS);
    }

    public void setPotionSickness(int sickness) {
        entityData.set(POTION_SICKNESS, sickness);
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getOwnerReference() {
        return (EntityReference) ((Optional) this.entityData.get(DATA_OWNERUUID_ID)).orElse(null);
    }

    public void setOwnerReference(EntityReference<LivingEntity> owner) {
        if (owner != null) {
            try {
                this.entityData.set(DATA_OWNERUUID_ID, Optional.of(owner));
            } catch (Throwable _) {}
        } else {
            this.entityData.set(DATA_OWNERUUID_ID, Optional.empty());
        }
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new BSFGolemSitWhenOrderedToGoal(this));
        goalSelector.addGoal(2, new BSFGolemFollowOwnerGoal(this, 1.0, 8.0F, 3.0F, 25.0F, 15.0F));
        goalSelector.addGoal(3, new BSFGolemRangedAttackGoal(this, 1.0, 30, 50.0F));
        goalSelector.addGoal(4, new BSFGolemRandomStrollGoal(this, 0.8, 1E-5F));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 5.0F));
        targetSelector.addGoal(1, new BSFGolemHurtByTargetGoal(this));
        targetSelector.addGoal(2, new BSFGolemOwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new BSFGolemOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(4, new BSFGolemNearsetAttackableTargetGoal(this));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        if (!pPlayer.equals(getOwner())) {
            return InteractionResult.PASS;
        }
        Level level = level();
        if (!level.isClientSide()) {
            ItemStack itemStack = pPlayer.getItemInHand(pHand);
            Item item = itemStack.getItem();
            if (item instanceof SnowballTankItem && getAmmo().isEmpty()) {
                setAmmo(itemStack.copy());
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                playSound(SoundEvents.ARMOR_EQUIP_IRON.value(), 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                Vec3 pos = getPosition(1);
                Vec3 vel = new Vec3(1, 0.5, 0.5);
                PacketDistributor.sendToPlayersTrackingEntity(this, new ForwardRaysParticlesPayload(new ForwardRaysParticlesParas(pos.add(-0.5, 0, -0.5), pos.add(0.5, 1, 0.5), vel, vel.length(), vel.length(), 30), BSFParticleType.SNOW_GOLEM_EQUIP.ordinal()));
            } else if ((item instanceof SnowballCannonItem || item instanceof SnowballShotgunItem) && getWeapon().isEmpty()) {
                setWeapon(itemStack.copy());
                if (!pPlayer.getAbilities().instabuild) {
                    if (EnchantmentHelper.getTagEnchantmentLevel(BSFEnchantmentHelper.getEnchantmentHolder(this, BSFEnchantmentHelper.SNOW_GOLEM_EXCLUSIVE), itemStack) > 0) {
                        itemStack.hurtAndBreak(10, pPlayer, pPlayer.getUsedItemHand());
                    } else {
                        itemStack.shrink(1);
                    }
                }
                playSound(SoundEvents.ARMOR_EQUIP_IRON.value(), 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                Vec3 pos = getPosition(1);
                Vec3 vel = new Vec3(0.5, 0.5, 1);
                PacketDistributor.sendToPlayersTrackingEntity(this, new ForwardRaysParticlesPayload(new ForwardRaysParticlesParas(pos.add(-0.5, 0, -0.5), pos.add(0.5, 1, 0.5), vel, vel.length(), vel.length(), 30), BSFParticleType.SNOW_GOLEM_EQUIP.ordinal()));
            } else if (itemStack.isEmpty()) {
                if (pPlayer.isShiftKeyDown()) {
                    if (!getWeapon().isEmpty()) {
                        playSound(SoundEvents.DISPENSER_DISPENSE, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    }
                    if (EnchantmentHelper.getTagEnchantmentLevel(BSFEnchantmentHelper.getEnchantmentHolder(this, BSFEnchantmentHelper.SNOW_GOLEM_EXCLUSIVE), getWeapon()) <= 0) {
                        pPlayer.getInventory().placeItemBackInInventory(getWeapon(), true);
                    }
                    setWeapon(ItemStack.EMPTY);
                } else {
                    if (!getAmmo().isEmpty()) {
                        playSound(SoundEvents.DISPENSER_DISPENSE, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    }
                    pPlayer.getInventory().placeItemBackInInventory(getAmmo(), true);
                    setAmmo(ItemStack.EMPTY);
                }
            } else if (item.equals(ItemRegistry.SMOOTH_SNOWBALL.get()) || item.equals(Items.POWDER_SNOW_BUCKET) || item.equals(Items.SNOW_BLOCK) || item.equals(Items.ICE)) {
                if (getPotionSickness() == 0) {
                    itemStack.shrink(1);
                    if (item.equals(ItemRegistry.SMOOTH_SNOWBALL.get())) {
                        heal(2);
                        setPotionSickness(20);
                        ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, getX(), getEyeY(), getZ(), 8, 0, 0, 0, 0.04);
                        playSound(SoundEvents.SNOW_BREAK, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    } else if (item.equals(Items.POWDER_SNOW_BUCKET)) {
                        pPlayer.getInventory().placeItemBackInInventory(new ItemStack(Items.BUCKET, 1), true);
                        heal(8);
                        setPotionSickness(100);
                        ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, getX(), getEyeY(), getZ(), 24, 0, 0, 0, 0.04);
                        playSound(SoundEvents.SNOW_BREAK, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    } else if (item.equals(Items.SNOW_BLOCK)) {
                        heal(5);
                        setPotionSickness(60);
                        ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, getX(), getEyeY(), getZ(), 16, 0, 0, 0, 0.04);
                        playSound(SoundEvents.SNOW_BREAK, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    } else {
                        addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
                        setPotionSickness(60);
                        ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, getX(), getEyeY(), getZ(), 16, 0, 0, 0, 0.04);
                        playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    }
                    ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER, getX(), getY() + 1, getZ(), 7, 0.4, 0.5, 0.4, 0.05);
                    this.playSound(SoundEvents.SNOW_PLACE, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                } else {
                    pPlayer.sendSystemMessage(Component.translatable("potionSickness.tip", String.valueOf(getPotionSickness())));
                }
            } else if (item.equals(ItemRegistry.SNOW_GOLEM_MODE_TWEAKER.get())) {
                int targetMode = itemStack.getOrDefault(DataComponentRegistry.TWEAKER_TARGET_MODE, (byte) 0);
                int statusMode = itemStack.getOrDefault(DataComponentRegistry.TWEAKER_STATUS_MODE, (byte) 0);
                if (targetMode != getLocator()) {
                    setTarget(null);
                }
                setLocator((byte) targetMode);
                setStatus((byte) statusMode);
                pPlayer.sendSystemMessage(Component.translatable("import_state.tip"));
                Vec3 pos = getPosition(1);
                Vec3 vel = new Vec3(0.5, 1, 0.5);
                PacketDistributor.sendToPlayersTrackingEntity(this, new ForwardRaysParticlesPayload(new ForwardRaysParticlesParas(pos.add(-0.5, 0, -0.5), pos.add(0.5, 1, 0.5), vel, vel.length(), vel.length(), 30), BSFParticleType.SNOW_GOLEM_EQUIP.ordinal()));
                level.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.DISPENSER_DISPENSE, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            } else if (item instanceof SnowballClampItem) {
                if (item.equals(ItemRegistry.EMERALD_SNOWBALL_CLAMP.get())) {
                    pPlayer.getInventory().placeItemBackInInventory(ItemRegistry.DUCK_SNOWBALL.get().getDefaultInstance(), true);
                } else {
                    pPlayer.getInventory().placeItemBackInInventory(ItemRegistry.SMOOTH_SNOWBALL.get().getDefaultInstance(), true);
                }
                itemStack.hurtAndBreak(1, pPlayer, pHand);
            } else if (item.equals(Items.SNOWBALL)) {
                setStyle((byte) ((getStyle() + 1) % STYLE_NUM));
                ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, getX(), getY() + 1, getZ(), 20, 0, 0.5, 0, 0.05);
                this.playSound(SoundEvents.SNOW_PLACE, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            } else if (item.equals(ItemRegistry.CREATIVE_SNOW_GOLEM_TOOL.get())) {
                if (pPlayer.isShiftKeyDown()) {
                    itemStack.set(DataComponentRegistry.SNOW_GOLEM_DATA, getReconstructData());
                    pPlayer.sendSystemMessage(Component.translatable("copy.tip"));
                } else {
                    setEnhance(!getEnhance());
                    pPlayer.sendSystemMessage(Component.translatable("golem_enhance.tip", String.valueOf(getEnhance())));
                    Vec3 pos = getPosition(1);
                    Vec3 vel = new Vec3(1, 0.8, 0.5);
                    PacketDistributor.sendToPlayersTrackingEntity(this, new ForwardRaysParticlesPayload(new ForwardRaysParticlesParas(pos.add(-0.5, 0, -0.5), pos.add(0.5, 1, 0.5), vel, vel.length(), vel.length(), 30), BSFParticleType.SNOW_GOLEM_EQUIP.ordinal()));
                }
                level.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.DISPENSER_DISPENSE, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            } else if (item.equals(ItemRegistry.SNOW_GOLEM_CONTAINER.get())) {
                if (!itemStack.has(DataComponentRegistry.SNOW_GOLEM_DATA)) {
                    itemStack.set(DataComponentRegistry.SNOW_GOLEM_DATA, getReconstructData());
                    playSound(SoundEvents.SNOW_BREAK);
                    ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, getX(), getY() + 1, getZ(), 20, 0, 0.5, 0, 0.05);
                    discard();
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick() {
        Level level = level();
        if (!level.isClientSide()) {
            if (getPotionSickness() > 0) {
                setPotionSickness(getPotionSickness() - 1);
            }
            LivingEntity target = getTarget();
            if (target == null) {
                entityData.set(TARGET_NAME, Optional.empty());
            } else {
                entityData.set(TARGET_NAME, Optional.of(target.getName()));
            }
        }
        super.tick();
    }

    public CompoundTag getReconstructData() {
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), Main.LOGGER)) {
            TagValueOutput entityData = TagValueOutput.createWithContext(reporter, registryAccess());
            saveWithoutId(entityData);
            CompoundTag tag1 = entityData.buildResult();
            tag1.remove("Pos");
            tag1.remove("Motion");
            tag1.remove("UUID");
            return tag1;
        }
    }

    public boolean isEntityHasSameOwner(@Nullable LivingEntity pTarget) {
        if (pTarget == null) {
            return false;
        }
        return pTarget instanceof OwnableEntity ownableEntity && Objects.equals(getOwner(), ownableEntity.getOwner());
    }

    @Override
    public boolean shouldConsumeAmmo() {
        return !(getAmmo().has(DataComponents.UNBREAKABLE) || getEnhance());
    }

    @Override
    public boolean shouldDamageWeapon() {
        ItemStack weapon = getWeapon();
        float damageChance = 1.0F / (1.0F + EnchantmentHelper.getTagEnchantmentLevel(BSFEnchantmentHelper.getEnchantmentHolder(this, Enchantments.UNBREAKING), weapon));
        return !(weapon.has(DataComponents.UNBREAKABLE) || getEnhance() || getRandom().nextFloat() > damageChance);
    }

    @Override
    public boolean canMoveAndAttack() {
        return getStatus() == 2 || getStatus() == 3;
    }
}

package com.linngdu664.bsf3lite.entity.golem;

import com.linngdu664.bsf3lite.entity.ai.goal.HostileGolemRangedAttackGoal;
import com.linngdu664.bsf3lite.entity.ai.goal.target.HostileGolemHurtByTargetGoal;
import com.linngdu664.bsf3lite.entity.ai.goal.target.HostileGolemNearestAttackableTargetGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HostileSnowGolemEntity extends AbstractBSFSnowGolemEntity implements Enemy {
    public HostileSnowGolemEntity(EntityType<? extends AbstractBSFSnowGolemEntity> entityType, Level level) {
        super(entityType, level);
        setDropSnowball(false);
        setDropEquipment(false);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new HostileGolemRangedAttackGoal(this, 1.0, 30, 50.0F));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8, 1E-5F));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 5.0F));
        targetSelector.addGoal(1, new HostileGolemHurtByTargetGoal(this));
        targetSelector.addGoal(2, new HostileGolemNearestAttackableTargetGoal(this));
    }

    @Override
    public void tick() {
        Level level = level();
        if (!level.isClientSide() && isAlive()) {
            hurt(level.damageSources().genericKill(), Float.MAX_VALUE);
        }
        super.tick();
    }

    @Override
    public boolean shouldConsumeAmmo() {
        return false;
    }

    @Override
    public boolean shouldDamageWeapon() {
        return false;
    }

    @Override
    public boolean canMoveAndAttack() {
        return true;
    }
}

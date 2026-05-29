package com.linngdu664.bsf3lite.entity.ai.goal.target;

import com.linngdu664.bsf3lite.entity.golem.BSFSnowGolemEntity;
import com.linngdu664.bsf3lite.item.component.RegionData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;

import java.util.EnumSet;

public class BSFGolemOwnerHurtByTargetGoal extends TargetGoal {
    private final BSFSnowGolemEntity snowGolem;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public BSFGolemOwnerHurtByTargetGoal(BSFSnowGolemEntity snowGolem) {
        super(snowGolem, true);
        this.snowGolem = snowGolem;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    // 0: monster
    // 1: designate
    public boolean canUse() {
        LivingEntity owner = snowGolem.getOwner();
        if (snowGolem.getStatus() == 0 || owner == null) {
            return false;
        }
        ownerLastHurtBy = owner.getLastHurtByMob();
        RegionData aliveRange = snowGolem.getAliveRange();
        if (aliveRange != null && ownerLastHurtBy != null && !aliveRange.inRegion(ownerLastHurtBy.position())) {
            return false;
        }
        if (snowGolem.getLocator() == 0) {
            if (!(ownerLastHurtBy instanceof Enemy)) {
                return false;
            }
        } else if (owner.equals(ownerLastHurtBy) || snowGolem.isEntityHasSameOwner(ownerLastHurtBy)) {
            return false;
        }

        int $$1 = owner.getLastHurtByMobTimestamp();
        return $$1 != timestamp && canAttack(ownerLastHurtBy, TargetingConditions.DEFAULT);
    }

    public void start() {
        mob.setTarget(ownerLastHurtBy);
        LivingEntity owner = snowGolem.getOwner();
        if (owner != null) {
            timestamp = owner.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}

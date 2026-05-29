package com.linngdu664.bsf3lite.entity.ai.goal.target;

import com.linngdu664.bsf3lite.entity.golem.BSFSnowGolemEntity;
import com.linngdu664.bsf3lite.item.component.RegionData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;

public class BSFGolemHurtByTargetGoal extends HurtByTargetGoal {
    private final BSFSnowGolemEntity snowGolem;

    public BSFGolemHurtByTargetGoal(BSFSnowGolemEntity snowGolem, Class<?>... pToIgnoreDamage) {
        super(snowGolem, pToIgnoreDamage);
        this.snowGolem = snowGolem;
    }

    // 0: monster
    // 1: designate
    @Override
    public boolean canUse() {
        LivingEntity lastHurtByMob = snowGolem.getLastHurtByMob();
        RegionData aliveRange = snowGolem.getAliveRange();
        if (lastHurtByMob == null) {
            return false;
        }
        if (aliveRange != null && !aliveRange.inRegion(lastHurtByMob.position())) {
            return false;
        }
        if (snowGolem.getLocator() == 0) {
            if (lastHurtByMob instanceof Enemy) return super.canUse();
            return false;
        }
        return !lastHurtByMob.equals(snowGolem.getOwner()) && !snowGolem.isEntityHasSameOwner(lastHurtByMob) && super.canUse();
    }
}

package com.linngdu664.bsf3lite.entity.ai.goal.target;

import com.linngdu664.bsf3lite.entity.golem.HostileSnowGolemEntity;
import com.linngdu664.bsf3lite.item.component.RegionData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class HostileGolemHurtByTargetGoal extends HurtByTargetGoal {
    private final HostileSnowGolemEntity snowGolem;

    public HostileGolemHurtByTargetGoal(HostileSnowGolemEntity snowGolem, Class<?>... pToIgnoreDamage) {
        super(snowGolem, pToIgnoreDamage);
        this.snowGolem = snowGolem;
    }

    @Override
    public boolean canUse() {
        LivingEntity lastHurtByMob = snowGolem.getLastHurtByMob();
        RegionData aliveRange = snowGolem.getAliveRange();
        if (aliveRange != null && lastHurtByMob != null && !aliveRange.inRegion(lastHurtByMob.position())) {
            return false;
        }
        if (lastHurtByMob instanceof HostileSnowGolemEntity) {
            return false;
        }
        return super.canUse();
    }
}

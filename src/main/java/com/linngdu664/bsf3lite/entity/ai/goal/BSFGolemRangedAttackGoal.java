package com.linngdu664.bsf3lite.entity.ai.goal;

import com.linngdu664.bsf3lite.entity.golem.BSFSnowGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;

public class BSFGolemRangedAttackGoal extends HostileGolemRangedAttackGoal {
    private final BSFSnowGolemEntity golem;

    public BSFGolemRangedAttackGoal(BSFSnowGolemEntity golem, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
        super(golem, pSpeedModifier, pAttackInterval, pAttackRadius);
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        if (golem.getStatus() != 0 && golem.getStatus() != 1) {
            return super.canUse();
        }
        return false;
    }

    @Override
    protected void changeTargetWhenNecessary(LivingEntity entity) {
        int locator = golem.getLocator();
        if (locator == 0) {
            if (entity instanceof Enemy) {
                golem.setTarget(entity);
            }
        }
        attackTime = 1;
    }
}

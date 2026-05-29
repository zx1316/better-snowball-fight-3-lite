package com.linngdu664.bsf3lite.entity.ai.goal;

import com.linngdu664.bsf3lite.entity.golem.BSFSnowGolemEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BSFGolemSitWhenOrderedToGoal extends Goal {
    private final BSFSnowGolemEntity golem;

    public BSFGolemSitWhenOrderedToGoal(BSFSnowGolemEntity golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canContinueToUse() {
        return golem.getStatus() == 0;
    }

    @Override
    public boolean canUse() {
        if (golem.isInWater()) {
            return false;
        } else if (!golem.onGround()) {
            return false;
        } else {
            return golem.getStatus() == 0;    // 特殊模式无视主人
        }
    }

    @Override
    public void start() {
        golem.getNavigation().stop();
    }
}

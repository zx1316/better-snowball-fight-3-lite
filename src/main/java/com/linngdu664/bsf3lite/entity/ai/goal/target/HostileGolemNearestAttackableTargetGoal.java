package com.linngdu664.bsf3lite.entity.ai.goal.target;

import com.linngdu664.bsf3lite.entity.golem.HostileSnowGolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class HostileGolemNearestAttackableTargetGoal extends TargetGoal {
    private static final int DEFAULT_RANDOM_INTERVAL = 4;
    private static final int SEARCH_DISTANCE = 100;
    private final HostileSnowGolemEntity snowGolem;
    protected LivingEntity target;

    public HostileGolemNearestAttackableTargetGoal(HostileSnowGolemEntity snowGolem) {
        super(snowGolem, false, false);
        this.snowGolem = snowGolem;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        if (mob.getRandom().nextInt(DEFAULT_RANDOM_INTERVAL) != 0) {
            return false;
        }
        TargetingConditions targetConditions = TargetingConditions.forCombat().range(SEARCH_DISTANCE);
        targetConditions.ignoreLineOfSight();
        ServerLevel level = getServerLevel(mob);
        targetConditions.selector((e, _) -> !(e instanceof HostileSnowGolemEntity));
        target = level.getNearestEntity(level.getEntitiesOfClass(LivingEntity.class, snowGolem.getTargetSearchArea(SEARCH_DISTANCE), e -> true), targetConditions, snowGolem, snowGolem.getX(), snowGolem.getEyeY(), snowGolem.getZ());
        return target != null;
    }

    public void start() {
        mob.setTarget(target);
        super.start();
    }
}

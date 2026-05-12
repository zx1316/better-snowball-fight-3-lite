package com.linngdu664.bsf3lite.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

public class ColdResistanceEffect extends MobEffect {
    public ColdResistanceEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NonNull ServerLevel serverLevel, LivingEntity mob, int amplification) {
        int t = mob.getTicksFrozen();
        if (t > 5) {
            mob.setTicksFrozen(t - 5);
        } else {
            mob.setTicksFrozen(0);
        }
        return true;
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}

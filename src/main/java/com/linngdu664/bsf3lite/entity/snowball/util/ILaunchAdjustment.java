package com.linngdu664.bsf3lite.entity.snowball.util;

public interface ILaunchAdjustment {
    float adjustPunch(float punch);

    int adjustWeaknessTicks(int weaknessTicks);

    int adjustFrozenTicks(int frozenTicks);

    float adjustDamage(float damage);

    float adjustBlazeDamage(float blazeDamage);

    LaunchFrom getLaunchFrom();
}

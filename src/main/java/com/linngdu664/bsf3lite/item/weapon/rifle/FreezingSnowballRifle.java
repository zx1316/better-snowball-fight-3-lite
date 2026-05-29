package com.linngdu664.bsf3lite.item.weapon.rifle;

import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.entity.snowball.util.LaunchFrom;
import com.linngdu664.bsf3lite.item.snowball.normal.IceSnowballItem;
import com.linngdu664.bsf3lite.item.snowball.special.CriticalFrozenSnowballItem;
import com.linngdu664.bsf3lite.item.snowball.special.FrozenSnowballItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class FreezingSnowballRifle extends SnowballRifleItem {
    public FreezingSnowballRifle() {
        super("freezing_snowball_rifle", Rarity.UNCOMMON);
    }

    @Override
    public ILaunchAdjustment getLaunchAdjustment(double damageDropRate, Item snowball) {
        return new ILaunchAdjustment() {
            @Override
            public double adjustPunch(double punch) {
                return punch + 1.2;
            }

            @Override
            public int adjustWeaknessTicks(int weaknessTicks) {
                return weaknessTicks;
            }

            @Override
            public int adjustFrozenTicks(int frozenTicks) {
                return frozenTicks + 140;
            }

            @Override
            public float adjustDamage(float damage) {
                return damage;
            }

            @Override
            public float adjustBlazeDamage(float blazeDamage) {
                if (snowball instanceof IceSnowballItem || snowball instanceof FrozenSnowballItem || snowball instanceof CriticalFrozenSnowballItem) {
                    return blazeDamage + 4;
                }
                return blazeDamage + 1;
            }

            @Override
            public LaunchFrom getLaunchFrom() {
                return LaunchFrom.FREEZING_CANNON;
            }
        };
    }
}

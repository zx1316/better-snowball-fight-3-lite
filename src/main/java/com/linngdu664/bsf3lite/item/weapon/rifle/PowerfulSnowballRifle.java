package com.linngdu664.bsf3lite.item.weapon.rifle;

import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.entity.snowball.util.LaunchFrom;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PowerfulSnowballRifle extends SnowballRifleItem {
    public PowerfulSnowballRifle() {
        super("powerful_snowball_rifle", Rarity.UNCOMMON);
    }

    @Override
    public ILaunchAdjustment getLaunchAdjustment(float damageDropRate, Item snowball) {
        return new ILaunchAdjustment() {
            @Override
            public float adjustPunch(float punch) {
                return punch + 2F;
            }

            @Override
            public int adjustWeaknessTicks(int weaknessTicks) {
                return weaknessTicks + 180;
            }

            @Override
            public int adjustFrozenTicks(int frozenTicks) {
                return frozenTicks;
            }

            @Override
            public float adjustDamage(float damage) {
                return damage;
            }

            @Override
            public float adjustBlazeDamage(float blazeDamage) {
                return blazeDamage;
            }

            @Override
            public LaunchFrom getLaunchFrom() {
                return LaunchFrom.POWERFUL_CANNON;
            }
        };
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        return launch(level, player, usedHand, 3.467F);
    }
}

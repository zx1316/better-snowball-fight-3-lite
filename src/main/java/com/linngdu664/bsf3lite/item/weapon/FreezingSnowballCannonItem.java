package com.linngdu664.bsf3lite.item.weapon;

import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.entity.snowball.util.LaunchFrom;
import com.linngdu664.bsf3lite.item.snowball.normal.IceSnowballItem;
import com.linngdu664.bsf3lite.item.snowball.special.CriticalFrozenSnowballItem;
import com.linngdu664.bsf3lite.item.snowball.special.FrozenSnowballItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

import static com.linngdu664.bsf3lite.registry.KeyMappingRegistry.CYCLE_MOVE_AMMO_NEXT;
import static com.linngdu664.bsf3lite.registry.KeyMappingRegistry.CYCLE_MOVE_AMMO_PREV;

public class FreezingSnowballCannonItem extends SnowballCannonItem {
    public FreezingSnowballCannonItem() {
        super("freezing_snowball_cannon", Rarity.RARE);
    }

    @Override
    public ILaunchAdjustment getLaunchAdjustment(double damageDropRate, Item snowball) {
        return new ILaunchAdjustment() {
            @Override
            public double adjustPunch(double punch) {
                return punch + damageDropRate * 1.51;
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

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("snowball_cannon1.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("snowball_cannon2.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("snowball_cannon2_1.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("guns1.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("guns2.tooltip", CYCLE_MOVE_AMMO_PREV.getTranslatedKeyMessage(), CYCLE_MOVE_AMMO_NEXT.getTranslatedKeyMessage()).withStyle(ChatFormatting.DARK_GRAY));
    }
}

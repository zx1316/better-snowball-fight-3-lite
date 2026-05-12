package com.linngdu664.bsf3lite.item.misc;

import com.linngdu664.bsf3lite.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MilkPopsicleItem extends Item {
    public MilkPopsicleItem() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("milk_popsicle")))
                .food(new FoodProperties.Builder().alwaysEdible().build()));
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack itemStack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 64;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity user) {
        if (user instanceof Player player) {
            if (!level.isClientSide()) {
                player.setRemainingFireTicks(0);
                player.removeEffectsCuredBy(EffectCures.MILK);
                player.setTicksFrozen(40);
                player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 1));
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
            }
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("milk_popsicle.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

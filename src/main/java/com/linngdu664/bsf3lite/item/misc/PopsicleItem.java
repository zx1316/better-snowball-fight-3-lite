package com.linngdu664.bsf3lite.item.misc;

import com.linngdu664.bsf3lite.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PopsicleItem extends Item {
    public PopsicleItem() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("popsicle")))
                .food(new FoodProperties.Builder().alwaysEdible().build(), Consumables.defaultDrink().build()));
    }

    protected PopsicleItem(String id, Consumable consumable) {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier(id)))
                .food(new FoodProperties.Builder().alwaysEdible().build(), consumable));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity user) {
        ItemStack stack1 = super.finishUsingItem(stack, level, user);
        if (user instanceof Player player) {
            if (!level.isClientSide()) {
                user.setRemainingFireTicks(0);
                user.setTicksFrozen(40);
                user.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 1));
            }
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return stack1;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("popsicle.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

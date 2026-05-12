package com.linngdu664.bsf3lite.item.misc;

import com.linngdu664.bsf3lite.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class SnowballCannonUpgradeSmithingTemplateItem extends Item {
    public SnowballCannonUpgradeSmithingTemplateItem() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("snowball_cannon_upgrade_smithing_template")))
                .rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("snowball_cannon_upgrade_smithing_template1.tooltip").withStyle(ChatFormatting.BLUE));
        builder.accept(Component.translatable("snowball_cannon_upgrade_smithing_template2.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("void.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("snowball_cannon_upgrade_smithing_template3.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("snowball_cannon_upgrade_smithing_template4.tooltip").withStyle(ChatFormatting.BLUE));
        builder.accept(Component.translatable("snowball_cannon_upgrade_smithing_template5.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("snowball_cannon_upgrade_smithing_template6.tooltip").withStyle(ChatFormatting.BLUE));
    }
}

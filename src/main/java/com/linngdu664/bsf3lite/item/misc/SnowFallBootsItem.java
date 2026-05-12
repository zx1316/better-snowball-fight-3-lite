package com.linngdu664.bsf3lite.item.misc;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.misc.BSFArmorMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Consumer;

public class SnowFallBootsItem extends Item {
    public SnowFallBootsItem() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("snow_fall_boots")))
                .rarity(Rarity.UNCOMMON)
                .humanoidArmor(BSFArmorMaterials.SNOW_FALL_BOOTS, ArmorType.BOOTS)
                .repairable(Items.LEATHER_BOOTS));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("snow_fall_boots.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("snow_fall_boots1.tooltip", Component.translatable("enchantment.bsf.kinetic_energy_storage")).withStyle(ChatFormatting.GRAY));
    }
}

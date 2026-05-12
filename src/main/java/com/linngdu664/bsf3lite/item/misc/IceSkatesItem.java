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

public class IceSkatesItem extends Item {
    public IceSkatesItem() {
        super(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("ice_skates")))
                .rarity(Rarity.UNCOMMON)
                .humanoidArmor(BSFArmorMaterials.ICE_SKATES, ArmorType.BOOTS)
                .repairable(Items.LEATHER_BOOTS));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("ice_skates.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

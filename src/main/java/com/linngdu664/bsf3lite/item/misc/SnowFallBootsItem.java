package com.linngdu664.bsf3lite.item.misc;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.client.model.SnowFallBootsModel;
import com.linngdu664.bsf3lite.misc.BSFArmorMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class SnowFallBootsItem extends Item {
    public SnowFallBootsItem() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("snow_fall_boots")))
                .rarity(Rarity.UNCOMMON)
                .humanoidArmor(BSFArmorMaterials.SNOW_FALL_BOOTS, ArmorType.LEGGINGS)
                .repairable(Items.LEATHER_BOOTS));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of(
                        "left_leg", new SnowFallBootsModel(Minecraft.getInstance().getEntityModels().bakeLayer(SnowFallBootsModel.LAYER_LOCATION)).bone,
                        "right_leg", new SnowFallBootsModel(Minecraft.getInstance().getEntityModels().bakeLayer(SnowFallBootsModel.LAYER_LOCATION)).bone,
                        "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                armorModel.crouching = livingEntity.isShiftKeyDown();
                armorModel.riding = original.riding;
                armorModel.young = livingEntity.isBaby();
                return armorModel;
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("snow_fall_boots.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("snow_fall_boots1.tooltip", Component.translatable("enchantment.bsf.kinetic_energy_storage")).withStyle(ChatFormatting.GRAY));
    }
}

package com.linngdu664.bsf3lite.misc;

import com.google.common.collect.Maps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class BSFArmorMaterials {
    public static final ArmorMaterial ICE_SKATES = new ArmorMaterial(5, makeDefense(1, 2, 3, 1, 3), 1, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, ModTags.Items.NONE, EquipmentAssets.LEATHER);
    public static final ArmorMaterial SNOW_FALL_BOOTS = new ArmorMaterial(16, makeDefense(1, 2, 3, 1, 3), 17, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, ModTags.Items.NONE, EquipmentAssets.LEATHER);


    private static Map<ArmorType, Integer> makeDefense(int boots, int legs, int chest, int helm, int body) {
        return Maps.newEnumMap(Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs, ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm, ArmorType.BODY, body));
    }
}

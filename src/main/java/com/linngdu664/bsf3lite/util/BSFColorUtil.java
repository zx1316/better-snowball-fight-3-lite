package com.linngdu664.bsf3lite.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;

public class BSFColorUtil {
    public static String getColorNameKeyById(int id) {
        return "color.minecraft." + DyeColor.byId(id).getName();
    }

    public static Component getColorTransNameById(int id) {
        return Component.translatable(getColorNameKeyById(id));
    }
}

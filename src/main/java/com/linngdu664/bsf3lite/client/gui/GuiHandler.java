package com.linngdu664.bsf3lite.client.gui;

import com.linngdu664.bsf3lite.item.weapon.AbstractBSFWeaponItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballMachineGunItem;
import com.linngdu664.bsf3lite.registry.DataComponentRegister;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public class GuiHandler {
    public static void itemInHandBSFWeapon(GuiGraphicsExtractor guiGraphics, ItemStack mainHandItem, ItemStack offHandItem) {
        Minecraft instance = Minecraft.getInstance();
        AbstractBSFWeaponItem weaponItem = null;
        ItemStack selectItem = null;
        if (mainHandItem.getItem() instanceof AbstractBSFWeaponItem item) {
            weaponItem = item;
            selectItem = mainHandItem;
        } else if (offHandItem.getItem() instanceof AbstractBSFWeaponItem item) {
            weaponItem = item;
            selectItem = offHandItem;
        }
        if (weaponItem != null) {
            Window window = instance.getWindow();
            ItemStack current = weaponItem.getCurrentAmmoItemStack();
            ItemStack prev = weaponItem.getPrevAmmoItemStack();
            ItemStack next = weaponItem.getNextAmmoItemStack();
            V2I v2I = BSFGuiTool.SNOWBALL_SLOT_FRAME_GUI.renderCenterVertically(guiGraphics, window, 0);
            int startPos = v2I.y();
            guiGraphics.item(prev, 3, startPos + 3);
            guiGraphics.item(current, 3, startPos + 23);
            guiGraphics.item(next, 3, startPos + 43);
            guiGraphics.text(instance.font, String.valueOf(prev.getCount()), 24, startPos + 7, 0xffffffff);
            guiGraphics.text(instance.font, String.valueOf(current.getCount()), 24, startPos + 27, 0xffffffff);
            guiGraphics.text(instance.font, String.valueOf(next.getCount()), 24, startPos + 47, 0xffffffff);
            if (weaponItem.getTypeFlag() == SnowballMachineGunItem.TYPE_FLAG) {
                V2I barFrame = new V2I(100, 10);
                V2I barPos = new V2I(GuiUtil.widthFrameCenter(window, barFrame.x()), GuiUtil.heightFrameRatio(window, barFrame.y(), 0.7));
                int timer = selectItem.getOrDefault(DataComponentRegister.MACHINE_GUN_TIMER, 0);
                boolean isCoolDown = selectItem.getOrDefault(DataComponentRegister.MACHINE_GUN_IS_COOL_DOWN, false);
                int padding = 2;
                BSFGuiTool.renderProgressBar(guiGraphics, barPos, barFrame, padding, 0xffffffff, isCoolDown ? 0xfffc3d49 : 0xffffffff, (float) timer / 360);
            }
        }
    }
}

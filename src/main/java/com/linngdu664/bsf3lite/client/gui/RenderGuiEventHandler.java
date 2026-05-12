package com.linngdu664.bsf3lite.client.gui;

import com.linngdu664.bsf3lite.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class RenderGuiEventHandler {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft instance = Minecraft.getInstance();
        if (instance.options.hideGui) {
            return;
        }
        Player player = instance.player;
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();
        GuiGraphicsExtractor guiGraphics = event.getGuiGraphics();
//        guiGraphics.pose().pushPose();
//        guiGraphics.pose().translate(0F, 0F, 4932F);        // 显示在原版gui的上方
        //gui队列
        GuiHandler.itemInHandBSFWeapon(guiGraphics, mainHandItem, offHandItem);
//        guiGraphics.pose().popPose();
    }
}

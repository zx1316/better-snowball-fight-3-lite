package com.linngdu664.bsf3lite.client.gui;

import com.linngdu664.bsf3lite.client.gui.texture.Textures;
import com.linngdu664.bsf3lite.client.gui.util.HudContext;
import com.linngdu664.bsf3lite.util.V2I;
import com.linngdu664.bsf3lite.entity.BSFDummyEntity;
import com.linngdu664.bsf3lite.entity.golem.BSFSnowGolemEntity;
import com.linngdu664.bsf3lite.item.tool.SnowGolemModeTweakerItem;
import com.linngdu664.bsf3lite.item.weapon.AbstractBSFWeaponItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballMachineGunItem;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.registry.EntityRegistry;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
            V2I v2I = Textures.SNOWBALL_FRAME.renderVerticalCenter(guiGraphics, window, 0);
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
                int timer = selectItem.getOrDefault(DataComponentRegistry.MACHINE_GUN_TIMER, 0);
                boolean isCoolDown = selectItem.getOrDefault(DataComponentRegistry.MACHINE_GUN_IS_COOL_DOWN, false);
                int padding = 2;
                BSFGuiTool.renderProgressBar(guiGraphics, barPos, barFrame, padding, 0xffffffff, isCoolDown ? 0xfffc3d49 : 0xffffffff, (float) timer / 360);
            }
        }
    }

    public static void pickEntityBSFSnowGolem(GuiGraphicsExtractor guiGraphics, CoordinateConverter converter, Entity pickEntity, float partialTick, HudContext ctx) {
        Minecraft instance = Minecraft.getInstance();
        Player player = instance.player;
        if (pickEntity.getType().equals(EntityRegistry.BSF_SNOW_GOLEM.get()) && player.equals(((BSFSnowGolemEntity) pickEntity).getOwner())) {
            Window window = instance.getWindow();
            BSFSnowGolemEntity entity = (BSFSnowGolemEntity) pickEntity;
            // 显示装备
            List<Pair<Vec3, Consumer<Vec2>>> list = new ArrayList<>();
            Vec3 entityPosition = entity.getPosition(partialTick);
            Vec3 viewVector0Y = entity.getMiddleModelForward(partialTick, 0);
            ItemStack weapon = entity.getWeapon();
            if (weapon != ItemStack.EMPTY) {
                list.add(new Pair<>(entityPosition.add(entity.getMiddleModelForward(partialTick, 4).scale(0.7).add(0, 1.3, 0)), v2 -> {
                    V2I v2IRatio = GuiUtil.v2IRatio(window, Textures.EQUIPMENT_SLOT_FRAME_GUI.width(), Textures.EQUIPMENT_SLOT_FRAME_GUI.height(), 0.3, 0.3);
                    BSFGuiTool.renderEquipmentInfo(guiGraphics, v2, v2IRatio.toVec2f(), GuiUtil.widthWinRatio(window, 0.1), 0xffffffff, weapon, instance.font, Component.translatable("weapon.tip"));
                    float percent = (float) (weapon.getMaxDamage() - weapon.getDamageValue()) / weapon.getMaxDamage();
                    BSFGuiTool.renderProgressBar(guiGraphics, new V2I(v2IRatio.x() - 4, v2IRatio.y() + 23), new V2I(30, 6), 2, 0xffffffff, percent > 0.3 ? 0xff85e900 : 0xfffc3d49, percent);
                }));
            }
            ItemStack ammo = entity.getAmmo();
            if (ammo != ItemStack.EMPTY) {
                list.add(new Pair<>(entityPosition.add(viewVector0Y.scale(-0.3).add(0, 1.2, 0)), v2 -> {
                    V2I v2IRatio = GuiUtil.v2IRatio(window, Textures.EQUIPMENT_SLOT_FRAME_GUI.width(), Textures.EQUIPMENT_SLOT_FRAME_GUI.height(), 0.3, 0.5);
                    BSFGuiTool.renderEquipmentInfo(guiGraphics, v2, v2IRatio.toVec2f(), GuiUtil.widthWinRatio(window, 0.07), 0xffffffff, ammo, instance.font, Component.translatable("snowball.tip"));
                    float percent = (float) (ammo.getMaxDamage() - ammo.getDamageValue()) / ammo.getMaxDamage();
                    BSFGuiTool.renderProgressBar(guiGraphics, new V2I(v2IRatio.x() - 4, v2IRatio.y() + 23), new V2I(30, 6), 2, 0xffffffff, percent > 0.3 ? 0xff85e900 : 0xfffc3d49, percent);
                }));
            }
            converter.convertAndConsume(list, guiGraphics.guiWidth(), guiGraphics.guiHeight());
            // 显示模式
            byte locator = entity.getLocator();
            byte status = entity.getStatus();
            ctx.sLocatorComponent = Component.translatable(SnowGolemModeTweakerItem.locatorMap(locator));
            ctx.sStatusComponent = Component.translatable(SnowGolemModeTweakerItem.statusMap(status));
            V2I locateV2I = Textures.GOLEM_LOCATOR_GUI.renderRatio(guiGraphics, window, 0.7, 0.5);
            locateV2I = new V2I(locateV2I.x() - 1, locateV2I.y() - 1 + locator * 20);
            Textures.GOLEM_SELECTOR_GUI.render(guiGraphics, locateV2I.x(), locateV2I.y());
            V2I statusV2I = Textures.GOLEM_STATUS_GUI.renderRatio(guiGraphics, window, 0.7, 0.5, 60, 0);
            statusV2I = new V2I(statusV2I.x() - 1, statusV2I.y() - 1 + status * 20);
            Textures.GOLEM_SELECTOR_GUI.render(guiGraphics, statusV2I.x(), statusV2I.y());
            if (entity.getEnhance()) {
                Textures.ADVANCE_MODE_GUI.renderRatio(guiGraphics, window, 0.5, 0.8);
            }
            ctx.locateV2I = locateV2I;
            ctx.statusV2I = statusV2I;

            // 显示血条/cd
            V2I barFrame = new V2I(100, 10);
            int padding = 2;
            V2I barPos = new V2I(GuiUtil.widthFrameCenter(window, barFrame.x()), GuiUtil.heightFrameRatio(window, barFrame.y(), 0.1));
            BSFGuiTool.renderProgressBar(guiGraphics, barPos, barFrame, padding, 0xffffffff, 0xffe82f27, entity.getHealth() / entity.getMaxHealth());
            if (entity.getPotionSickness() > 0) {
                barPos = new V2I(barPos.x(), barPos.y() + 15);
                BSFGuiTool.renderProgressBar(guiGraphics, barPos, barFrame, padding, 0xffffffff, 0xff62df86, (float) entity.getPotionSickness() / 100);
            }

            // 显示当前目标
            Optional<Component> targetName = entity.getTargetName();
            V2I v2I = GuiUtil.v2IRatio(window, 0.4, 0.75);
            Component transComp = Component.translatable("tweaker_target_now.tip", targetName.orElseGet(() -> Component.translatable("snow_golem_target_null.tip")));
            guiGraphics.text(instance.font, transComp, v2I.x() - instance.font.width(transComp), v2I.y(), 0xffffffff);
        }
    }

    public static void pickEntityBSFDummy(GuiGraphicsExtractor guiGraphics, Entity pickEntity) {
        if (pickEntity.getType().equals(EntityRegistry.BSF_DUMMY.get())) {
            Minecraft instance = Minecraft.getInstance();
            Window window = instance.getWindow();
            BSFDummyEntity dummy = (BSFDummyEntity) pickEntity;
            V2I v2I = GuiUtil.v2IRatio(window, 0.4, 0.5);
            String dpsStr = String.format(dummy.getDPS() < 10 ? "DPS: %.2f" : "DPS: %.3g", dummy.getDPS());
            guiGraphics.text(instance.font, dpsStr, v2I.x() - instance.font.width(dpsStr), v2I.y() - 5, 0xffffffff);
        }
    }

    public static void itemInHandSnowGolemModeTweaker(GuiGraphicsExtractor guiGraphics, ItemStack mainHandItem, ItemStack offHandItem, HudContext ctx) {
        ItemStack tweaker = null;
        if (mainHandItem.getItem() instanceof SnowGolemModeTweakerItem) {
            tweaker = mainHandItem;
        } else if (offHandItem.getItem() instanceof SnowGolemModeTweakerItem) {
            tweaker = offHandItem;
        }
        if (tweaker != null) {
            Minecraft instance = Minecraft.getInstance();
            Window window = instance.getWindow();
            // 显示模式调整器gui
            byte locator = tweaker.getOrDefault(DataComponentRegistry.TWEAKER_TARGET_MODE, (byte) 0);
            ctx.tLocatorComponent = Component.translatable(SnowGolemModeTweakerItem.locatorMap(locator));
            byte status = tweaker.getOrDefault(DataComponentRegistry.TWEAKER_STATUS_MODE, (byte) 0);
            ctx.tStatusComponent = Component.translatable(SnowGolemModeTweakerItem.statusMap(status));
            V2I locateV2IT = Textures.TWEAKER_LOCATOR_GUI.renderRatio(guiGraphics, window, 0.7, 0.5, 30, 0);
            locateV2IT = new V2I(locateV2IT.x() - 1, locateV2IT.y() - 1 + locator * 20);
            Textures.TWEAKER_SELECTOR_GUI.render(guiGraphics, locateV2IT.x(), locateV2IT.y());
            V2I statusV2IT = Textures.TWEAKER_STATUS_GUI.renderRatio(guiGraphics, window, 0.7, 0.5, 90, 0);
            statusV2IT = new V2I(statusV2IT.x() - 1, statusV2IT.y() - 1 + status * 20);
            Textures.TWEAKER_SELECTOR_GUI.render(guiGraphics, statusV2IT.x(), statusV2IT.y());
            V2I locateV2I = ctx.locateV2I;
            if (locateV2I != null && locateV2I.y() != locateV2IT.y()) {
                Textures.SETTER_ARROW_GUI.render(guiGraphics, locateV2I.x() + 23, locateV2IT.y() + 2);
            }
            V2I statusV2I = ctx.statusV2I;
            if (statusV2I != null && statusV2I.y() != statusV2IT.y()) {
                Textures.SETTER_ARROW_GUI.render(guiGraphics, statusV2I.x() + 23, statusV2IT.y() + 2);
            }
        }
    }

    public static void specialModeText(GuiGraphicsExtractor guiGraphics, HudContext ctx) {
        Component sLocatorComponent = ctx.sLocatorComponent;
        Component tLocatorComponent = ctx.tLocatorComponent;
        Component sStatusComponent = ctx.sStatusComponent;
        Component tStatusComponent = ctx.tStatusComponent;
        if (!(sLocatorComponent == null && tLocatorComponent == null)) {
            Minecraft instance = Minecraft.getInstance();
            Window window = instance.getWindow();
            // 显示模式调整文字
            Component lStr = Component.translatable("tweaker_target.tip", sLocatorComponent == null ? tLocatorComponent : tLocatorComponent == null || sLocatorComponent.equals(tLocatorComponent) ? sLocatorComponent : sLocatorComponent.getString() + " << " + tLocatorComponent.getString());
            Component sStr = Component.translatable("tweaker_status.tip", sStatusComponent == null ? tStatusComponent : tStatusComponent == null || sStatusComponent.equals(tStatusComponent) ? sStatusComponent : sStatusComponent.getString() + " << " + tStatusComponent.getString());
            V2I v2I = GuiUtil.v2IRatio(window, 0.6, 0.75);
            guiGraphics.text(instance.font, lStr, v2I.x(), v2I.y(), 0xffffffff);
            guiGraphics.text(instance.font, sStr, v2I.x(), v2I.y() + 10, 0xffffffff);
        }
    }
}

package com.linngdu664.bsf3lite.event;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.client.gui.CoordinateConverter;
import com.linngdu664.bsf3lite.client.gui.GuiHandler;
import com.linngdu664.bsf3lite.client.gui.util.HudContext;
import com.linngdu664.bsf3lite.client.screenshake.ScreenshakeHandler;
import com.linngdu664.bsf3lite.item.tool.ColdCompressionJetEngineItem;
import com.linngdu664.bsf3lite.item.weapon.AbstractBSFWeaponItem;
import com.linngdu664.bsf3lite.item.weapon.cannon.SnowballCannonItem;
import com.linngdu664.bsf3lite.network.to_server.SculkSnowballLauncherSwitchSoundPayload;
import com.linngdu664.bsf3lite.network.to_server.SwitchTweakerStatusModePayload;
import com.linngdu664.bsf3lite.network.to_server.SwitchTweakerTargetModePayload;
import com.linngdu664.bsf3lite.registry.ItemRegistry;
import com.linngdu664.bsf3lite.registry.KeyMappingRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedHashSet;

@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class ClientGameEvents {
    public static final RandomSource BSF_RANDOM_SOURCE = RandomSource.create();

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.is(ItemRegistry.SCULK_SNOWBALL_LAUNCHER.get()) && player.isShiftKeyDown()) {
            ClientPacketDistributor.sendToServer(new SculkSnowballLauncherSwitchSoundPayload(event.getScrollDeltaY() > 0));
            event.setCanceled(true);
        } else if (itemStack.is(ItemRegistry.SNOW_GOLEM_MODE_TWEAKER.get()) && minecraft.options.keyShift.isDown()) {
            ClientPacketDistributor.sendToServer(new SwitchTweakerTargetModePayload(event.getScrollDeltaY() < 0));
            event.setCanceled(true);
        } else if (itemStack.is(ItemRegistry.SNOW_GOLEM_MODE_TWEAKER.get()) && minecraft.options.keySprint.isDown()) {
            ClientPacketDistributor.sendToServer(new SwitchTweakerStatusModePayload(event.getScrollDeltaY() < 0));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        int key = event.getKey();
        if (minecraft.screen == null && (key == KeyMappingRegistry.CYCLE_MOVE_AMMO_NEXT.getKey().getValue() || key == KeyMappingRegistry.CYCLE_MOVE_AMMO_PREV.getKey().getValue()) && event.getAction() == GLFW.GLFW_PRESS) {
            Player player = minecraft.player;
            AbstractBSFWeaponItem weaponItem = null;
            if (player.getMainHandItem().getItem() instanceof AbstractBSFWeaponItem item) {
                weaponItem = item;
            } else if (player.getOffhandItem().getItem() instanceof AbstractBSFWeaponItem item) {
                weaponItem = item;
            }
            if (weaponItem != null) {
                LinkedHashSet<Item> launchOrder = weaponItem.getLaunchOrder();
                if (!launchOrder.isEmpty()) {
                    if (key == KeyMappingRegistry.CYCLE_MOVE_AMMO_NEXT.getKey().getValue()) {
                        Item item = launchOrder.getFirst();
                        launchOrder.removeFirst();
                        launchOrder.addLast(item);
                    } else {
                        Item item = launchOrder.getLast();
                        launchOrder.removeLast();
                        launchOrder.addFirst(item);
                    }
                    player.playSound(SoundEvents.DISPENSER_DISPENSE, 1.0F, 1.0F / (player.level().getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                }
            }
        }
    }

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
        HitResult pick = instance.hitResult;
        HitResult.Type pickType = pick.getType();
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        CoordinateConverter converter = new CoordinateConverter();
        HudContext hudCtx = new HudContext();

        // gui队列
        GuiHandler.itemInHandBSFWeapon(guiGraphics, mainHandItem, offHandItem);
        if (pickType == HitResult.Type.ENTITY) {
            Entity entity1 = ((EntityHitResult) pick).getEntity();
            GuiHandler.pickEntityBSFSnowGolem(guiGraphics, converter, entity1, partialTick, hudCtx);
            GuiHandler.pickEntityBSFDummy(guiGraphics, entity1);
        }
        if (!player.isSpectator()) {
            GuiHandler.itemInHandSnowGolemModeTweaker(guiGraphics, mainHandItem, offHandItem, hudCtx);
        }
        GuiHandler.specialModeText(guiGraphics, hudCtx);
    }

    @SubscribeEvent
    public static void onComputeFovModifier(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getUseItem();
        if (player.isUsingItem()) {
            if (itemStack.getItem() instanceof SnowballCannonItem) {
                int i = player.getTicksUsingItem();
                float f = event.getFovModifier();
                float f1 = (float) i / 20.0F;
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                } else {
                    f1 *= f1;
                }
                if (itemStack.is(ItemRegistry.POWERFUL_SNOWBALL_CANNON.get())) {
                    f *= 1.0F - f1 * 0.5F;
                } else {
                    f *= 1.0F - f1 * 0.3F;
                }
                event.setNewFovModifier(f);
            } else if (itemStack.getItem() instanceof ColdCompressionJetEngineItem) {
                float f = event.getFovModifier();
                if (player.getTicksUsingItem() >= ColdCompressionJetEngineItem.STARTUP_DURATION) {
                    f *= 1.4F;
                }
                event.setNewFovModifier(f);
            }
        }
    }

    @SubscribeEvent
    public static void onClientTickPost(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null || minecraft.isPaused()) {
            return;
        }
        // tick camera
        Camera camera = minecraft.gameRenderer.getMainCamera();
        ScreenshakeHandler.clientTick(camera, null);
        ScreenshakeHandler.clientTick(camera, BSF_RANDOM_SOURCE);

        // tick weapons
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        Inventory inventory = player.getInventory();
        for (int i = 0, size = inventory.getContainerSize(); i < size; i++) {
            ItemStack itemStack = inventory.getItem(i);
            // 40 = offhand
            if (itemStack.getItem() instanceof AbstractBSFWeaponItem weapon) {
                weapon.inventoryTickInClient(itemStack, player, i);
            }
        }
    }
}

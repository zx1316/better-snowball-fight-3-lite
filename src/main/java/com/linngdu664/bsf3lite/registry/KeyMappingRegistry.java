package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class KeyMappingRegistry {
    public static final KeyMapping CYCLE_MOVE_AMMO_NEXT = new KeyMapping("key.bsf3lite.ammo_switch_next", GLFW.GLFW_KEY_H, KeyMapping.Category.MISC);
    public static final KeyMapping CYCLE_MOVE_AMMO_PREV = new KeyMapping("key.bsf3lite.ammo_switch_prev", GLFW.GLFW_KEY_G, KeyMapping.Category.MISC);

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(CYCLE_MOVE_AMMO_NEXT);
        event.register(CYCLE_MOVE_AMMO_PREV);
    }
}

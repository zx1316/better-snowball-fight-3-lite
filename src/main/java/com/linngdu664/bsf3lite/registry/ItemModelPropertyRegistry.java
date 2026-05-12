package com.linngdu664.bsf3lite.registry;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.client.renderer.item.properties.numeric.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;

@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class ItemModelPropertyRegistry {
    @SubscribeEvent
    public static void registerRangeSelectItemModelProperty(RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(Main.makeMyIdentifier("sc_xxx"), ScXXX.MAP_CODEC);
        event.register(Main.makeMyIdentifier("sc_starting"), ScStarting.MAP_CODEC);
        event.register(Main.makeMyIdentifier("cooling"), Cooling.MAP_CODEC);
        event.register(Main.makeMyIdentifier("snowball"), Snowball.MAP_CODEC);
        event.register(Main.makeMyIdentifier("snow_type"), SnowType.MAP_CODEC);
    }
}

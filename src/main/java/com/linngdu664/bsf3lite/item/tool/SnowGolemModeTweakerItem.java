package com.linngdu664.bsf3lite.item.tool;

import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class SnowGolemModeTweakerItem extends Item {
    public SnowGolemModeTweakerItem() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("snow_golem_mode_tweaker")))
                .stacksTo(1)
                .component(DataComponentRegistry.TWEAKER_STATUS_MODE, (byte) 0)
                .component(DataComponentRegistry.TWEAKER_TARGET_MODE, (byte) 0)
        );
    }

    public static String locatorMap(byte lc) {
        if (lc == 0) {
            return "snow_golem_locator_monster.tip";
        }
        return "snow_golem_locator_specify.tip";
    }

    public static String statusMap(byte st) {
        return switch (st) {
            case 0 -> "snow_golem_standby.tip";
            case 1 -> "snow_golem_follow.tip";
            case 2 -> "snow_golem_follow_and_attack.tip";
            case 3 -> "snow_golem_attack.tip";
            default -> "snow_golem_turret.tip";
        };
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        Options options = Minecraft.getInstance().options;
        builder.accept(Component.translatable("snow_golem_mode_tweaker.tooltip", options.keyShift.getTranslatedKeyMessage()).withStyle(ChatFormatting.DARK_GRAY));
        builder.accept(Component.translatable("snow_golem_mode_tweaker1.tooltip", options.keySprint.getTranslatedKeyMessage()).withStyle(ChatFormatting.DARK_GRAY));
        builder.accept(Component.translatable("snow_golem_mode_tweaker2.tooltip").withStyle(ChatFormatting.BLUE));
        builder.accept(Component.translatable("tweaker_target.tip", Component.translatable(locatorMap(itemStack.getOrDefault(DataComponentRegistry.TWEAKER_TARGET_MODE, (byte) 0)))).withStyle(ChatFormatting.DARK_GRAY));
        builder.accept(Component.translatable("tweaker_status.tip", Component.translatable(statusMap(itemStack.getOrDefault(DataComponentRegistry.TWEAKER_STATUS_MODE, (byte) 0)))).withStyle(ChatFormatting.DARK_GRAY));
    }
}

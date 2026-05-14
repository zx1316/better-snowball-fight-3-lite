package com.linngdu664.bsf3lite.item.minigame_tool;

import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class RegionToolItem extends Item {
    public RegionToolItem() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier("region_tool")))
                .stacksTo(1)
                .component(DataComponentRegistry.REGION, RegionData.EMPTY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemInHand = context.getItemInHand();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (!level.isClientSide()) {
            BlockPos blockPos = context.getClickedPos();
            RegionData regionData = itemInHand.getOrDefault(DataComponentRegistry.REGION, RegionData.EMPTY);
            if (!player.isShiftKeyDown()) {
                itemInHand.set(DataComponentRegistry.REGION, new RegionData(blockPos, regionData.end()));
                player.sendSystemMessage(Component.literal("start: (" + blockPos.toShortString() + ")"));
            } else {
                itemInHand.set(DataComponentRegistry.REGION, new RegionData(regionData.start(), blockPos));
                player.sendSystemMessage(Component.literal("end: (" + blockPos.toShortString() + ")"));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && hand.equals(InteractionHand.MAIN_HAND)) {
            ItemStack offhandItem = player.getOffhandItem();
            ItemStack mainHandItem = player.getMainHandItem();
            offhandItem.set(DataComponentRegistry.REGION, mainHandItem.getOrDefault(DataComponentRegistry.REGION, RegionData.EMPTY));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        RegionData region = itemStack.getOrDefault(DataComponentRegistry.REGION.get(), RegionData.EMPTY);
        builder.accept(Component.translatable(
                "scoring_device_region.tooltip",
                region.start().getX(),
                region.start().getY(),
                region.start().getZ(),
                region.end().getX(),
                region.end().getY(),
                region.end().getZ()
        ));
        builder.accept(Component.literal("mode: " + (region.start().getY() > region.end().getY() ? "spawn point" : "golem")));
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }
}

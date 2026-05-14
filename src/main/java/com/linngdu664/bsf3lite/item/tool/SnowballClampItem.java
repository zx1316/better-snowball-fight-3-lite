package com.linngdu664.bsf3lite.item.tool;

import com.linngdu664.bsf3lite.misc.BSFToolMaterials;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.registry.ItemRegistry;
import com.linngdu664.bsf3lite.Main;
import com.linngdu664.bsf3lite.misc.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SnowballClampItem extends Item {
    private final boolean isForDuck;

    public SnowballClampItem(String id, ToolMaterial material, int durability) {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, Main.makeMyIdentifier(id)))
                .tool(material, ModTags.Blocks.NONE, -1.0F, -2.0F, 0F)
                .durability(durability));
        isForDuck = material == BSFToolMaterials.EMERALD;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        ItemStack itemStack = pContext.getItemInHand();
        Level level = pContext.getLevel();
        Block block = level.getBlockState(pContext.getClickedPos()).getBlock();
        if ((block == Blocks.SNOW_BLOCK || block == Blocks.SNOW || block == Blocks.POWDER_SNOW) && player != null) {
            if (player.getMainHandItem().isEmpty() || player.getOffhandItem().isEmpty()) {
                ItemStack stack;
                if (isForDuck) {
                    stack = ItemRegistry.DUCK_SNOWBALL.get().getDefaultInstance();
                } else {
                    stack = ItemRegistry.SMOOTH_SNOWBALL.get().getDefaultInstance();
                }
                if (itemStack.has(DataComponentRegistry.REGION.get())) {
                    stack.set(DataComponentRegistry.REGION.get(), itemStack.get(DataComponentRegistry.REGION.get()));
                }
                player.getInventory().placeItemBackInInventory(stack, true);
                itemStack.hurtAndBreak(1, player, pContext.getHand());
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof SnowGolem && (pPlayer.getMainHandItem().isEmpty() || pPlayer.getOffhandItem().isEmpty())) {
            if (isForDuck) {
                pPlayer.getInventory().placeItemBackInInventory(ItemRegistry.DUCK_SNOWBALL.get().getDefaultInstance(), true);
            } else {
                pPlayer.getInventory().placeItemBackInInventory(ItemRegistry.SMOOTH_SNOWBALL.get().getDefaultInstance(), true);
            }
            pStack.hurtAndBreak(1, pPlayer, pUsedHand);
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("snowball_clamp.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

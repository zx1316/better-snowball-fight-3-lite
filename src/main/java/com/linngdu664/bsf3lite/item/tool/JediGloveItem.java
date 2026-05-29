package com.linngdu664.bsf3lite.item.tool;

import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class JediGloveItem extends GloveItem {
    public JediGloveItem() {
        super("jedi_glove", Rarity.UNCOMMON, 514, 12);
    }

    @Override
    public void onUseTick(@NotNull Level pLevel, @NotNull LivingEntity pLivingEntity, @NotNull ItemStack pStack, int pRemainingUseDuration) {
        if (!pLevel.isClientSide() && pLivingEntity instanceof Player player) {
            AABB aabb = player.getBoundingBox().inflate(3);
            List<AbstractBSFSnowballEntity> list = pLevel.getEntitiesOfClass(AbstractBSFSnowballEntity.class, aabb, p -> !player.equals(p.getOwner()) && p.canBeCaught());
            List<Snowball> list1 = pLevel.getEntitiesOfClass(Snowball.class, aabb, p -> !player.equals(p.getOwner()));
            // todo check power
            PowerParticleOption particleOption = PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1f);
            for (AbstractBSFSnowballEntity snowball : list) {
                ItemStack itemStack = snowball.getItem();
                if (snowball.getRegion() != null) {
                    itemStack.set(DataComponentRegistry.REGION.get(), snowball.getRegion());
                }
                player.getInventory().placeItemBackInInventory(itemStack, true);
                ((ServerLevel) pLevel).sendParticles(particleOption, snowball.getX(), snowball.getY(), snowball.getZ(), 8, 0, 0, 0, 0.05);
                snowball.discard();
            }
            for (Snowball snowball : list1) {
                player.getInventory().placeItemBackInInventory(snowball.getItem(), true);
                ((ServerLevel) pLevel).sendParticles(particleOption, snowball.getX(), snowball.getY(), snowball.getZ(), 8, 0, 0, 0, 0.05);
                snowball.discard();
            }
            if (!list.isEmpty() || !list1.isEmpty()) {
                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL, 3F, 0.4F / pLevel.getRandom().nextFloat() * 0.4F + 0.8F);
                releaseUsing(pStack, pLevel, pLivingEntity, 0);
            }
            pStack.hurtAndBreak(list.size() + list1.size(), player, player.getUsedItemHand());
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("jedi_glove.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

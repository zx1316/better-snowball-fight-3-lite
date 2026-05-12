package com.linngdu664.bsf3lite.item.snowball.special;

import com.linngdu664.bsf3lite.config.ServerConfig;
import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.special.ReconstructSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ReconstructSnowballItem extends AbstractSnowStorageSnowballItem {
    public ReconstructSnowballItem() {
        super("reconstruct_snowball", Rarity.RARE, new SnowballProperties().idForTank(18));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        return throwOrStorage(pPlayer, pLevel, pUsedHand, 2.0F, 15);
    }

    @Override
    public AbstractBSFSnowballEntity getCorrespondingEntity(Level level, LivingEntity livingEntity, ILaunchAdjustment launchAdjustment, RegionData region) {
        return new ReconstructSnowballEntity(livingEntity, level, launchAdjustment, Math.min(absorbSnow(livingEntity, level), ServerConfig.RECONSTRUCT_SNOWBALL_CAPACITY.getConfigValue()), region);
    }

    @Override
    public void addMainTips(Consumer<Component> builder) {
        super.addMainTips(builder);
        builder.accept(Component.translatable("reconstruct_snowball.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

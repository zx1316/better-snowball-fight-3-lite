package com.linngdu664.bsf3lite.item.snowball.special;

import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.special.ExpansionSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.item.snowball.AbstractBSFSnowballItem;
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

public class ExpansionSnowballItem extends AbstractBSFSnowballItem {
    public ExpansionSnowballItem() {
        super("expansion_snowball", Rarity.UNCOMMON, new SnowballProperties().idForTank(17).allowLaunchTypeFlag(HAND_TYPE_FLAG));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        return throwOrStorage(pPlayer, pLevel, pUsedHand, 1.25F, 40);
    }

    @Override
    public AbstractBSFSnowballEntity getCorrespondingEntity(Level level, LivingEntity livingEntity, ILaunchAdjustment launchAdjustment, RegionData region) {
        return new ExpansionSnowballEntity(livingEntity, level, launchAdjustment, region);
    }

    @Override
    public void addMainTips(Consumer<Component> builder) {
        builder.accept(Component.translatable("expansion_snowball.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

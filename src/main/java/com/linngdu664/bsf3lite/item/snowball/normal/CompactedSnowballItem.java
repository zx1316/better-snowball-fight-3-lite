package com.linngdu664.bsf3lite.item.snowball.normal;

import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.nomal.CompactedSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.item.snowball.AbstractBSFSnowballItem;
import com.linngdu664.bsf3lite.item.weapon.SculkSnowballLauncherItem;
import com.linngdu664.bsf3lite.item.weapon.cannon.SnowballCannonItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballMachineGunItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballShotgunItem;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CompactedSnowballItem extends AbstractBSFSnowballItem implements ProjectileItem {
    public CompactedSnowballItem() {
        super("compacted_snowball", Rarity.COMMON, new SnowballProperties().idForTank(0).allowLaunchTypeFlag(AbstractBSFSnowballItem.HAND_TYPE_FLAG | SnowballCannonItem.TYPE_FLAG | SnowballShotgunItem.TYPE_FLAG | SnowballMachineGunItem.TYPE_FLAG | SculkSnowballLauncherItem.TYPE_FLAG));
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        return throwOrStorage(pPlayer, pLevel, pUsedHand, 1.5F, 0);
    }

    @Override
    public AbstractBSFSnowballEntity getCorrespondingEntity(Level level, LivingEntity livingEntity, ILaunchAdjustment launchAdjustment, RegionData region) {
        return new CompactedSnowballEntity(livingEntity, level, launchAdjustment, region);
    }

    @Override
    public @NotNull Projectile asProjectile(@NotNull Level level, @NotNull Position position, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        CompactedSnowballEntity snowball = new CompactedSnowballEntity(level, position.x(), position.y(), position.z(), itemStack.get(DataComponentRegistry.REGION));
        snowball.setItem(itemStack);
        return snowball;
    }

    @Override
    public void addMainTips(Consumer<Component> builder) {
        builder.accept(Component.translatable("compacted_snowball.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

package com.linngdu664.bsf3lite.item.snowball.normal;

import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.nomal.IceSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.item.snowball.AbstractBSFSnowballItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballCannonItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballMachineGunItem;
import com.linngdu664.bsf3lite.item.weapon.SnowballShotgunItem;
import com.linngdu664.bsf3lite.registry.DataComponentRegister;
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

public class IceSnowballItem extends AbstractBSFSnowballItem implements ProjectileItem {
    public IceSnowballItem() {
        super("ice_snowball", Rarity.COMMON, new SnowballProperties()
                .idForTank(4)
                .allowLaunchTypeFlag(AbstractBSFSnowballItem.HAND_TYPE_FLAG | SnowballCannonItem.TYPE_FLAG | SnowballShotgunItem.TYPE_FLAG | SnowballMachineGunItem.TYPE_FLAG)
                .machineGunRecoil(0.1)
                .shotgunPushRank(0.12)
        );
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        return throwOrStorage(pPlayer, pLevel, pUsedHand, 1.125F, 0);
    }

    @Override
    public AbstractBSFSnowballEntity getCorrespondingEntity(Level level, LivingEntity livingEntity, ILaunchAdjustment launchAdjustment, RegionData region) {
        return new IceSnowballEntity(livingEntity, level, launchAdjustment, region);
    }

    @Override
    public @NotNull Projectile asProjectile(@NotNull Level level, @NotNull Position position, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        IceSnowballEntity snowball = new IceSnowballEntity(level, position.x(), position.y(), position.z(), itemStack.get(DataComponentRegister.REGION));
        snowball.setItem(itemStack);
        return snowball;
    }

    @Override
    public void addMainTips(Consumer<Component> builder) {
        builder.accept(Component.translatable("ice_snowball.tooltip").withStyle(ChatFormatting.GRAY));
    }
}

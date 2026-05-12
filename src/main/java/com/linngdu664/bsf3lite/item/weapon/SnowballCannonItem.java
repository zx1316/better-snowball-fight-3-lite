package com.linngdu664.bsf3lite.item.weapon;

import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.entity.snowball.util.LaunchFrom;
import com.linngdu664.bsf3lite.item.component.ItemData;
import com.linngdu664.bsf3lite.item.snowball.AbstractBSFSnowballItem;
import com.linngdu664.bsf3lite.item.tank.SnowballTankItem;
import com.linngdu664.bsf3lite.network.to_client.ForwardConeParticlesPayload;
import com.linngdu664.bsf3lite.network.to_client.packed_paras.ForwardConeParticlesParas;
import com.linngdu664.bsf3lite.particle.util.BSFParticleType;
import com.linngdu664.bsf3lite.registry.DataComponentRegister;
import com.linngdu664.bsf3lite.registry.EffectRegister;
import com.linngdu664.bsf3lite.registry.SoundRegister;
import com.linngdu664.bsf3lite.util.BSFEnchantmentHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.linngdu664.bsf3lite.event.ClientModEvents.CYCLE_MOVE_AMMO_NEXT;
import static com.linngdu664.bsf3lite.event.ClientModEvents.CYCLE_MOVE_AMMO_PREV;

public class SnowballCannonItem extends AbstractBSFWeaponItem {
    public static final int TYPE_FLAG = 2;

    public SnowballCannonItem() {
        super("snowball_cannon", 514, Rarity.UNCOMMON, TYPE_FLAG);
    }

    public SnowballCannonItem(String id, Rarity rarity) {
        super(id, 514, rarity, TYPE_FLAG);
    }

    public float getPowerForTime(int pCharge) {
        float f = (float) pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    @Override
    public boolean isAllowBulkedSnowball() {
        return true;
    }

    @Override
    public ILaunchAdjustment getLaunchAdjustment(double damageDropRate, Item snowball) {
        return new ILaunchAdjustment() {
            @Override
            public double adjustPunch(double punch) {
                return punch + damageDropRate * 1.51;
            }

            @Override
            public int adjustWeaknessTicks(int weaknessTicks) {
                return weaknessTicks;
            }

            @Override
            public int adjustFrozenTicks(int frozenTicks) {
                return frozenTicks;
            }

            @Override
            public float adjustDamage(float damage) {
                return damage;
            }

            @Override
            public float adjustBlazeDamage(float blazeDamage) {
                return blazeDamage;
            }

            @Override
            public LaunchFrom getLaunchFrom() {
                return LaunchFrom.CANNON;
            }
        };
    }

    @Override
    public boolean releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
        return launch(pStack, pLevel, pEntityLiving, pTimeLeft, 3.0F);
    }

    public boolean launch(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, float velocity) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack, pEntityLiving) - pTimeLeft;
            float f = getPowerForTime(i);
            if (f >= 0.1F) {
                ItemStack itemStack = getAmmo(player, pStack);
                if (itemStack != null) {
                    AbstractBSFSnowballEntity snowballEntity = ItemToEntity(itemStack, player, pLevel, getLaunchAdjustment(f, itemStack.getItem()));
                    BSFShootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), f * velocity, 0.5F);
                    pLevel.addFreshEntity(snowballEntity);
                    pStack.hurtAndBreak(1, player, player.getUsedItemHand());
                    Vec3 cameraVec = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
                    if (pLevel.isClientSide()) {
                        // add push
                        Item item = itemStack.getItem();
                        if (item instanceof SnowballTankItem) {
                            item = itemStack.getOrDefault(DataComponentRegister.AMMO_ITEM, ItemData.EMPTY).item();
                        }
                        double pushRank = -0.05 * (double) velocity * (double) f * (1 + (((AbstractBSFSnowballItem) item).getMachineGunRecoil() - 0.1) * 4);
                        player.push(pushRank * cameraVec.x, pushRank * cameraVec.y, pushRank * cameraVec.z);
                    } else {
                        // add particles
                        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new ForwardConeParticlesPayload(new ForwardConeParticlesParas(player.getEyePosition(), cameraVec, 4.5F, 90, 1.5F, 0.1), BSFParticleType.SNOWFLAKE.ordinal()));
                        pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SNOWBALL_CANNON_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    }
                    consumeAmmo(itemStack, player);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(BSFEnchantmentHelper.getEnchantmentHolder(pPlayer, BSFEnchantmentHelper.SNOW_GOLEM_EXCLUSIVE), itemStack);
        if (enchantmentLevel <= 0 && !pPlayer.hasEffect(EffectRegister.WEAPON_JAM)) {
            pPlayer.startUsingItem(pHand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack pStack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("snowball_cannon1.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("guns1.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("guns2.tooltip", CYCLE_MOVE_AMMO_PREV.getTranslatedKeyMessage(), CYCLE_MOVE_AMMO_NEXT.getTranslatedKeyMessage()).withStyle(ChatFormatting.DARK_GRAY));
    }
}

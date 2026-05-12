package com.linngdu664.bsf3lite.item.weapon;

import com.linngdu664.bsf3lite.client.screenshake.Easing;
import com.linngdu664.bsf3lite.client.screenshake.ScreenshakeHandler;
import com.linngdu664.bsf3lite.client.screenshake.ScreenshakeInstance;
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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.linngdu664.bsf3lite.registry.KeyMappingRegistry.CYCLE_MOVE_AMMO_NEXT;
import static com.linngdu664.bsf3lite.registry.KeyMappingRegistry.CYCLE_MOVE_AMMO_PREV;

public class SnowballShotgunItem extends AbstractBSFWeaponItem {
    public static final int TYPE_FLAG = 8;

    public SnowballShotgunItem() {
        super("snowball_shotgun", 1145, Rarity.EPIC, TYPE_FLAG);
    }

    @Override
    public ILaunchAdjustment getLaunchAdjustment(double damageDropRate, Item snowball) {
        return new ILaunchAdjustment() {
            @Override
            public double adjustPunch(double punch) {
                return punch + 1.51;
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
                return LaunchFrom.SHOTGUN;
            }
        };
    }

    @Override
    public boolean isAllowBulkedSnowball() {
        return true;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.hasEffect(EffectRegister.WEAPON_JAM)) {
            return InteractionResult.FAIL;
        }
        double pushRank = 0;
        // add push or summon projectile
        int i;
        for (i = 0; i < 4; i++) {
            ItemStack itemStack = getAmmo(player, stack);
            if (itemStack == null) {
                break;
            }
            AbstractBSFSnowballEntity snowballEntity = ItemToEntity(itemStack, player, level, getLaunchAdjustment(1, itemStack.getItem()));
            BSFShootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), 2.0F, 10.0F);
            level.addFreshEntity(snowballEntity);
            Item item = itemStack.getItem();
            if (item instanceof SnowballTankItem) {
                item = itemStack.getOrDefault(DataComponentRegister.AMMO_ITEM, ItemData.EMPTY).item();
            }
            pushRank += ((AbstractBSFSnowballItem) item).getMachineGunRecoil() * 0.5;
            consumeAmmo(itemStack, player);
        }
        if (i == 0) {
            return InteractionResult.PASS;
        }
        // finally push player
        Vec3 cameraVec = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
        if (level.isClientSide()) {
            player.push(-pushRank * cameraVec.x, -pushRank * cameraVec.y, -pushRank * cameraVec.z);
            ScreenshakeHandler.addScreenshake((new ScreenshakeInstance(3)).setIntensity(0.8f).setEasing(Easing.ELASTIC_IN));
        } else {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new ForwardConeParticlesPayload(new ForwardConeParticlesParas(player.getEyePosition(), cameraVec, 4.5F, 45, 1.5F, 0.1), BSFParticleType.SNOWFLAKE.ordinal()));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_2.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            stack.hurtAndBreak(1, player, player.getUsedItemHand());
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        player.getCooldowns().addCooldown(stack, 20);
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("snowball_shotgun1.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("guns1.tooltip").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable("guns2.tooltip", CYCLE_MOVE_AMMO_PREV.getTranslatedKeyMessage(), CYCLE_MOVE_AMMO_NEXT.getTranslatedKeyMessage()).withStyle(ChatFormatting.DARK_GRAY));
    }
}

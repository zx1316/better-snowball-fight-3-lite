package com.linngdu664.bsf3lite.item.weapon.rifle;

import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.entity.snowball.util.LaunchFrom;
import com.linngdu664.bsf3lite.item.component.ItemData;
import com.linngdu664.bsf3lite.item.snowball.AbstractBSFSnowballItem;
import com.linngdu664.bsf3lite.item.tank.SnowballTankItem;
import com.linngdu664.bsf3lite.item.weapon.AbstractBSFWeaponItem;
import com.linngdu664.bsf3lite.network.to_client.ForwardConeParticlesPayload;
import com.linngdu664.bsf3lite.network.to_client.packed_paras.ForwardConeParticlesParas;
import com.linngdu664.bsf3lite.particle.util.BSFParticleType;
import com.linngdu664.bsf3lite.registry.DataComponentRegistry;
import com.linngdu664.bsf3lite.registry.EffectRegistry;
import com.linngdu664.bsf3lite.registry.SoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class SnowballRifleItem extends AbstractBSFWeaponItem {
    public static final int TYPE_FLAG = 2;

    public SnowballRifleItem() {
        super("snowball_rifle", 514, Rarity.COMMON, TYPE_FLAG);
    }

    protected SnowballRifleItem(String id, Rarity rarity) {
        super(id, 514, rarity, TYPE_FLAG);
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
                return punch + 1.2;
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

    public InteractionResult launch(Level level, Player player, @NotNull InteractionHand usedHand, float velocity) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.hasEffect(EffectRegistry.WEAPON_JAM)) {
            return InteractionResult.FAIL;
        }

        // add push or summon projectile
        ItemStack itemStack = getAmmo(player, stack);
        if (itemStack == null) {
            return InteractionResult.PASS;
        }
        AbstractBSFSnowballEntity snowballEntity = ItemToEntity(itemStack, player, level, getLaunchAdjustment(1, itemStack.getItem()));
        BSFShootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), velocity, 0.5F);
        level.addFreshEntity(snowballEntity);
        Item item = itemStack.getItem();
        if (item instanceof SnowballTankItem) {
            item = itemStack.getOrDefault(DataComponentRegistry.AMMO_ITEM, ItemData.EMPTY).item();
        }
        consumeAmmo(itemStack, player);

        // finally push player
        Vec3 cameraVec = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
        if (level.isClientSide()) {
            double pushRank = ((AbstractBSFSnowballItem) item).getMachineGunRecoil() * 0.5;
            player.push(-pushRank * cameraVec.x, -pushRank * cameraVec.y, -pushRank * cameraVec.z);
        } else {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new ForwardConeParticlesPayload(new ForwardConeParticlesParas(player.getEyePosition(), cameraVec, 4.5F, 45, 1.5F, 0.1), BSFParticleType.SNOWFLAKE.ordinal()));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegistry.SNOWBALL_CANNON_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            stack.hurtAndBreak(1, player, player.getUsedItemHand());
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        player.getCooldowns().addCooldown(stack, 20);
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        return launch(level, player, usedHand, 2.6F);
    }
}

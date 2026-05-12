package com.linngdu664.bsf3lite.entity.snowball.special;

import com.linngdu664.bsf3lite.config.ServerConfig;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.registry.EntityRegister;
import com.linngdu664.bsf3lite.registry.ItemRegister;
import com.linngdu664.bsf3lite.util.BSFCommonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ExpansionSnowballEntity extends AbstractConstructSnowballEntity {
    public ExpansionSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, ServerConfig.EXPANSION_SNOWBALL_DURATION.getConfigValue(), new BSFSnowballEntityProperties());
    }

    public ExpansionSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegister.EXPANSION_SNOWBALL.get(), pShooter, pLevel, ItemRegister.EXPANSION_SNOWBALL.toStack(), ServerConfig.EXPANSION_SNOWBALL_DURATION.getConfigValue(), new BSFSnowballEntityProperties().applyAdjustment(launchAdjustment), region);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        Level level = level();
        if (!level.isClientSide()) {
            if (!isCaught) {
                Vec3 vec3 = BSFCommonUtil.getRealHitPosOnMoveVecWithHitResult(this, pResult);
                BlockPos blockPos = new BlockPos(Mth.floor(vec3.x), Mth.floor(vec3.y), Mth.floor(vec3.z));
                float eighthPi = Mth.PI / 8;
                for (int i = 0; i < 16; i++) {
                    for (int j = -8; j <= 8; j++) {
                        int x = Mth.floor(3 * Mth.cos(i * eighthPi) * Mth.cos(j * eighthPi) + 0.5F);
                        int y = Mth.floor(3 * Mth.sin(j * eighthPi) + 0.5F);
                        int z = Mth.floor(3 * Mth.sin(i * eighthPi) * Mth.cos(j * eighthPi) + 0.5F);
                        BlockPos blockPos1 = blockPos.offset(x, y, z);
                        placeAndRecordBlock(level, blockPos1);
                        level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.SNOW_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    }
                }
                ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 200, 0, 0, 0, 0.32);
                startTimingOfDiscard();
            }
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegister.EXPANSION_SNOWBALL.get();
    }
}

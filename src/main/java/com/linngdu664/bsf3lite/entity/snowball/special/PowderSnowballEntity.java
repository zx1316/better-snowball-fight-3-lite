package com.linngdu664.bsf3lite.entity.snowball.special;

import com.linngdu664.bsf3lite.entity.executor.PowderExecutor;
import com.linngdu664.bsf3lite.entity.snowball.AbstractBSFSnowballEntity;
import com.linngdu664.bsf3lite.entity.snowball.util.ILaunchAdjustment;
import com.linngdu664.bsf3lite.item.component.RegionData;
import com.linngdu664.bsf3lite.registry.EntityRegistry;
import com.linngdu664.bsf3lite.registry.ItemRegistry;
import com.linngdu664.bsf3lite.registry.ParticleRegistry;
import com.linngdu664.bsf3lite.registry.SoundRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class PowderSnowballEntity extends AbstractBSFSnowballEntity {
    public PowderSnowballEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, new BSFSnowballEntityProperties());
    }

    public PowderSnowballEntity(Level pLevel, double pX, double pY, double pZ, RegionData region) {
        super(EntityRegistry.POWDER_SNOWBALL.get(), pX, pY, pZ, pLevel, ItemRegistry.POWDER_SNOWBALL.toStack(),
                new BSFSnowballEntityProperties().aliveRange(region));
    }

    public PowderSnowballEntity(LivingEntity pShooter, Level pLevel, ILaunchAdjustment launchAdjustment, RegionData region) {
        super(EntityRegistry.POWDER_SNOWBALL.get(), pShooter, pLevel, ItemRegistry.POWDER_SNOWBALL.toStack(),
                new BSFSnowballEntityProperties().applyAdjustment(launchAdjustment).aliveRange(region));
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        Level level = level();
        if (!level.isClientSide()) {
            ((ServerLevel) level).sendParticles(ParticleRegistry.BIG_LONG_TIME_SNOWFLAKE.get(), this.getX(), this.getY(), this.getZ(), 25, 0, 0, 0, 0.4);
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundRegistry.POWDER_SNOWBALL.get(), SoundSource.PLAYERS, 0.3F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            level.addFreshEntity(new PowderExecutor(EntityRegistry.POWDER_EXECUTOR.get(), getX(), getY(), getZ(), level(), getRegion()));
        }
        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (isCaught) {
            this.discard();
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegistry.POWDER_SNOWBALL.get();
    }
}
